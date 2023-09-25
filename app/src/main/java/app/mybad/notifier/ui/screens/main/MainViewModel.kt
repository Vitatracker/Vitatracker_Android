package app.mybad.notifier.ui.screens.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.usecases.usages.GetPatternUsagesWithNameAndDateBetweenUseCase
import app.mybad.domain.usecases.usages.GetUsagesWithNameAndDateBetweenUseCase
import app.mybad.domain.usecases.usages.SetFactUseTimeOrInsertUsageUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.betweenDaysSystem
import app.mybad.utils.changeTime
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.displayDateTimeShort
import app.mybad.utils.displayTimeInMinutes
import app.mybad.utils.isBetweenDay
import app.mybad.utils.isEqualsDay
import app.mybad.utils.systemToEpochSecond
import app.mybad.utils.timeInMinutes
import app.mybad.utils.toSystem
import app.mybad.utils.toUTC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPatternUsagesWithNameAndDateBetweenUseCase: GetPatternUsagesWithNameAndDateBetweenUseCase,
    private val getUsagesWithNameAndDateBetweenUseCase: GetUsagesWithNameAndDateBetweenUseCase,
    private val setFactUseTimeOrInsertUsageUseCase: SetFactUseTimeOrInsertUsageUseCase,
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    override fun setInitialState() = MainContract.State()

    override fun handleEvents(event: MainContract.Event) {
        Log.d("VTTAG", "MainViewModel::handleEvents: event=$event")
        when (event) {
            is MainContract.Event.ChangeDate -> changeDate(event.date)
            is MainContract.Event.SetUsageFactTime -> setUsagesFactTime(event.usageKey)
        }
    }

    private val dateTime = MutableStateFlow(viewState.value.selectedDate)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val patternsAndUsages = dateTime.flatMapLatest { date ->
        Log.w("VTTAG", "MainViewModel::patternsAndUsages: pattern + usages date=$date")
        val startDate = date.atStartOfDay().systemToEpochSecond()
        val endDate = date.atEndOfDay().systemToEpochSecond()
        combine(
            getPatternUsagesWithNameAndDateBetweenUseCase(
                startTime = startDate,
                endTime = endDate,
            ).mapLatest { transformPatternUsages(date, it) },
            getUsagesWithNameAndDateBetweenUseCase(
                startTime = startDate,
                endTime = endDate,
            ),
        ) { p, u ->
            // тут нужна подмена для useTime и timeInMinutes для pattern
//            p.plus(u).toSortedMap()
            // для тестов, потом удалить
            p.plus(u).mapValues {
                val pattern = it.value
                val timeInMinutesUTC = pattern.timeInMinutes
                val useTime = if (pattern.isPattern) date.toUTC().changeTime(pattern.timeInMinutes)
                    .toSystem() else pattern.useTime // с учетом часового пояса
                Log.w(
                    "VTTAG",
                    "MainViewModel::patternsAndUsages: isPattern=${pattern.isPattern} timeInMinutesUTC=${timeInMinutesUTC.displayTimeInMinutes()} - ${
                        useTime.timeInMinutes().displayTimeInMinutes()
                    }"
                )
                pattern.copy(
                    name = "${pattern.name}|${if (pattern.isPattern) "P" else "U"}|${useTime.displayDateTimeShort()}",
                    useTime = useTime,
                    timeInMinutes = useTime.timeInMinutes(), // с учетом часового пояса
                )

            }.toSortedMap()
        }
    }
        .distinctUntilChanged()
        .onEach(::changeState)

    init {
        observeAuthorization()
        receivingCourses()
    }

    private fun observeAuthorization() {
        viewModelScope.launch {
            AuthToken.isAuthorize.collect { isAuthorize ->
                if (!isAuthorize) setEffect { MainContract.Effect.Navigation.ToAuthorization }
            }
        }
    }

    private fun transformPatternUsages(
        date: LocalDateTime,
        patterns: List<UsageDisplayDomainModel>
    ): Map<String, UsageDisplayDomainModel> = patterns.filter { pattern ->
        date.isEqualsDay(pattern.startDate) ||
            (date.isBetweenDay(pattern.startDate, pattern.endDate) &&
                (pattern.regime == 0 || date.betweenDaysSystem(pattern.startDate) % (pattern.regime + 1) == 0L))
    }.associateBy { it.toUsageKey() }

    private fun receivingCourses() {
        viewModelScope.launch {
            //TODO("заменить и использованием жизненного цикла экрана")
            patternsAndUsages.collect()
        }
    }

    private fun changeState(pu: Map<String, UsageDisplayDomainModel>) {
        viewModelScope.launch {
            Log.w("VTTAG", "MainViewModel::receivingCourses: changeState=${pu.size}")
            setState {
                copy(
                    patternsAndUsages = pu,
                    selectedDate = dateTime.value,
                )
            }
        }
    }

    private fun changeDate(date: LocalDateTime) {
        viewModelScope.launch {
            dateTime.value = date
            Log.d("VTTAG", "MainViewModel::changeData: date=$date")
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
                    TODO("setUsagesFactTime: сделать обработку ошибок")
                }
            }
        }
    }

}
