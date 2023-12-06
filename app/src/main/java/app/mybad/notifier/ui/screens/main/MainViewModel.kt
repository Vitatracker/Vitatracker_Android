package app.mybad.notifier.ui.screens.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.usecases.courses.CountActiveCourseUseCase
import app.mybad.domain.usecases.patternusage.GetPatternUsagesActiveWithParamsBetween
import app.mybad.domain.usecases.patternusage.SetFactUseTimeOrInsertUsageUseCase
import app.mybad.domain.usecases.usages.GetUsagesWithNameAndDateBetweenUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atNoonOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.correctTimeInMinutes
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.displayDateTime
import app.mybad.utils.systemToEpochSecond
import app.mybad.utils.systemToInstant
import app.mybad.utils.timeInMinutes
import app.mybad.utils.toDateTimeSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPatternUsagesActiveWithParamsBetween: GetPatternUsagesActiveWithParamsBetween,
    private val getUsagesWithNameAndDateBetweenUseCase: GetUsagesWithNameAndDateBetweenUseCase,
    private val setFactUseTimeOrInsertUsageUseCase: SetFactUseTimeOrInsertUsageUseCase,
    private val countActiveCourseUseCase: CountActiveCourseUseCase,
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    override fun setInitialState() = MainContract.State()

    override fun handleEvents(event: MainContract.Event) {
        Log.d("VTTAG", "MainViewModel::handleEvents: event=$event")
        when (event) {
            is MainContract.Event.ChangeDate -> changeDate(event.date)
            is MainContract.Event.SetUsageFactTime -> setUsagesFactTime(event.usageKey)
        }
    }

    private val _dateTime = MutableStateFlow(currentDateTimeSystem().atNoonOfDay())

    @OptIn(ExperimentalCoroutinesApi::class)
    val dateTime = _dateTime.flatMapLatest { date ->
        val startDate = date.atStartOfDay().systemToEpochSecond()
        val endDate = date.atEndOfDay().systemToEpochSecond()
        Log.w(
            "VTTAG",
            "MainViewModel::patternsAndUsages: pattern + usages date=${date.displayDateTime()} startDate=${
                startDate.toDateTimeSystem().displayDateTime()
            } endDate=${endDate.toDateTimeSystem().displayDateTime()}"
        )
        combine(
            getPatternUsagesActiveWithParamsBetween(
                startTime = startDate,
                endTime = endDate,
            ).mapLatest(::transformPatternUsages),
            getUsagesWithNameAndDateBetweenUseCase(
                startTime = startDate,
                endTime = endDate,
            ),
        ) { p, u ->
            // тут нужна подмена для useTime и timeInMinutes для pattern
            p.plus(u).map {
                val pattern = it.value
                val useTime = if (pattern.isPattern) {
                    date.correctTimeInMinutes(pattern.timeInMinutes)
                } else pattern.useTime // с учетом часового пояса
//                Log.w(
//                    "VTTAG",
//                    "MainViewModel::patternsAndUsages: isPattern=${pattern.isPattern} useTime=${useTime.displayDateTime()} correct=${currentDateTimeSystem().timeCorrect()} time=${pattern.timeInMinutes.displayTimeInMinutes()} - ${
//                        pattern.timeInMinutes.timeCorrectToSystem().displayTimeInMinutes()
//                    } id=${pattern.id}"
//                )
                pattern.copy(
                    // для тестов, потом удалить name
//                    name = "${pattern.name}|${if (pattern.isPattern) "P" else "U"}|${useTime.displayDateTimeShort()}",
                    useTime = useTime,
                    timeInMinutes = useTime.timeInMinutes(), // с учетом часового пояса
                )

            }.sortedBy { it.useTime.systemToInstant() }.associateBy { it.toUsageKey() }
        }
    }
        .onEach(::changeState)
        .map { _dateTime.value }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = _dateTime.value
        )

    init {
        observeAuthorization()
    }

    private fun observeAuthorization() {
        viewModelScope.launch {
            AuthToken.isAuthorize.collect { isAuthorize ->
                if (!isAuthorize) setEffect { MainContract.Effect.Navigation.ToAuthorization }
            }
        }
    }

    private fun transformPatternUsages(
        patterns: List<UsageDisplayDomainModel>
    ): Map<String, UsageDisplayDomainModel> = patterns.filter { it.checkDate(_dateTime.value) }
        .associateBy { it.toUsageKey() }

    private fun changeState(pu: Map<String, UsageDisplayDomainModel>) {
        viewModelScope.launch {
            Log.w(
                "VTTAG",
                "MainViewModel::changeState: date=${_dateTime.value.displayDateTime()} changeState=${pu.size}"
            )
            val isEmpty = countActiveCourseUseCase(AuthToken.userId).getOrDefault(0L) == 0L
            setState {
                copy(
                    patternsAndUsages = pu,
                    updateDate = _dateTime.value,
                    isEmpty = isEmpty,
                )
            }
        }
    }

    private fun changeDate(date: LocalDateTime) {
        viewModelScope.launch {
            _dateTime.value = date.atNoonOfDay()
            Log.d("VTTAG", "MainViewModel::changeData: date=${date.displayDateTime()}")
        }
    }

    private fun setUsagesFactTime(usageKey: String) {
        viewModelScope.launch {
            viewState.value.patternsAndUsages[usageKey]?.let { usageDisplay ->
                val dateTime = currentDateTimeSystem()
                // тут отправка с проверкой на дублирование
                setFactUseTimeOrInsertUsageUseCase(
                    usageDisplay = usageDisplay,
                    currentDateTime = dateTime, // local
                    currentDateTimeUTC = dateTime.systemToEpochSecond(), // UTC
                    useTimeUTC = usageDisplay.useTime.systemToEpochSecond(), // UTC
                ).onFailure {
                    setEffect {
                        MainContract.Effect.Toast(
                            it.localizedMessage
                                ?: "Error: setFactTime ${usageDisplay.useTime.displayDateTime()}"
                        )
                    }
//                    TODO("setUsagesFactTime: сделать обработку ошибок")
                }
            } ?: setEffect { MainContract.Effect.Toast("Error: setFactTime - usage not found!") }
        }
    }

}
