package app.mybad.notifier.ui.screens.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.usecases.courses.CountActiveCourseUseCase
import app.mybad.domain.usecases.patternusage.GetPatternUsagesActiveWithParamsBetween
import app.mybad.domain.usecases.patternusage.SetFactUseTimeOrInsertUsageUseCase
import app.mybad.domain.usecases.usages.GetUsagesWithNameAndDateBetweenUseCase
import app.mybad.domain.usecases.usages.UpdateTimerUseCase
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
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
    private val updateTimerUseCase: UpdateTimerUseCase,
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    override fun setInitialState() = MainContract.State()

    override fun handleEvents(event: MainContract.Event) {
        log("handleEvents: event=$event")
        when (event) {
            is MainContract.Event.ChangeDate -> changeDate(event.date)
            is MainContract.Event.SetUsageFactTime -> setUsagesFactTime(event.usageKey)
        }
    }

    private val _updateTime = MutableStateFlow(currentDateTimeSystem())
    private val updateTime = _updateTime.onEach(::setTimer)

    private val selectDateToday: LocalDateTime? = null
    private val selectDateTime = MutableStateFlow(viewState.value.selectDate)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val usages = selectDateTime.flatMapLatest { date ->
        val startDate = date.atStartOfDay().systemToEpochSecond()
        val endDate = date.atEndOfDay().systemToEpochSecond()
        log(
            "patternsAndUsages: pattern + usages date=${date.displayDateTime()} startDate=${
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
            ::Pair
        ).map(::changeState)
    }

    val update = combine(
        usages,
        updateTime
    ) { _, date ->
        date
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = currentDateTimeSystem()
    )

    init {
        log("NotificationMonthPager: init ----------------------------------------")
        observeAuthorization()
    }

    fun setToday() {
        changeDate(currentDateTimeSystem())
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
    ): Map<String, UsageDisplayDomainModel> =
        patterns.filter { it.checkDate(selectDateTime.value) }
            .associateBy { it.toUsageKey() }

    private suspend fun changeState(
        pu: Pair<Map<String, UsageDisplayDomainModel>, Map<String, UsageDisplayDomainModel>>
    ) = pu.first.plus(pu.second).map {
        val pattern = it.value
        val useTime = if (pattern.isPattern) {
            selectDateTime.value.correctTimeInMinutes(pattern.timeInMinutes)
        } else pattern.useTime // с учетом часового пояса
        pattern.copy(
            // для тестов, потом удалить name
//                    name = "${pattern.name}|${if (pattern.isPattern) "P" else "U"}|${useTime.displayDateTimeShort()}",
            useTime = useTime,
            timeInMinutes = useTime.timeInMinutes(), // с учетом часового пояса
        )
    }.sortedBy { it.useTime.systemToInstant() }.associateBy { it.toUsageKey() }.also {
        log("changeState: date=${selectDateTime.value.displayDateTime()} changeState=${it.size}")
        val isEmpty = countActiveCourseUseCase(AuthToken.userId).getOrDefault(0L) == 0L
        setState {
            copy(
                patternsAndUsages = it,
                selectDate = selectDateTime.value,
                isEmpty = isEmpty,
            )
        }
        _updateTime.value = selectDateToday ?: currentDateTimeSystem()
    }

    private fun setTimer(date: LocalDateTime) {
        val usages = viewState.value.patternsAndUsages.values.toList()
        log("startUpdateTimer: in size=${usages.size} date=${date.displayDateTime()}")
        if (usages.isNotEmpty()) {
            val endTime = date.atEndOfDay()
            usages.firstOrNull {
                it.factUseTime == null && it.useTime > date && it.useTime <= endTime
            }?.let { usage ->
//                log("startUpdateTimer: start=${date.displayDateTime()} -> ${usage.useTime.displayDateTime()}")
                updateTimerUseCase.start(
                    usage.useTime.systemToEpochSecond(),
                    {
                        log("startUpdateTimer: tick=$it")
                    },
                    {
                        viewModelScope.launch {
                            updateTimerUseCase.stop()
                            _updateTime.value = currentDateTimeSystem()
                            log("startUpdateTimer: updateTime=${_updateTime.value.displayDateTime()}")
                        }
                    }
                )
            } ?: updateTimerUseCase.stop()
        } else updateTimerUseCase.stop()
        log("startUpdateTimer: out")
    }

    private fun changeDate(date: LocalDateTime) {
        selectDateTime.value = date.atNoonOfDay()
        log("changeData: date=${selectDateTime.value.displayDateTime()}")
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

    private fun log(text: String, e: Throwable? = null) {
        if (e == null) Log.w("VTTAG", "MainViewModel::$text")
        else Log.e("VTTAG", "MainViewModel::$text", e)
    }
}
