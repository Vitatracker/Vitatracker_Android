package app.mybad.notifier.ui.screens.calender

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.models.checkDate
import app.mybad.domain.usecases.usages.GetPatternUsagesWithParamsBetweenUseCase
import app.mybad.domain.usecases.usages.GetUsagesWithParamsBetweenUseCase
import app.mybad.domain.usecases.usages.SetFactUseTimeOrInsertUsageUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.DAYS_A_WEEK
import app.mybad.utils.WEEKS_PER_MONTH
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.changeTime
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.displayDateTime
import app.mybad.utils.displayDateTimeShort
import app.mybad.utils.displayDateTimeUTC
import app.mybad.utils.firstDayOfMonth
import app.mybad.utils.minusDays
import app.mybad.utils.plusDays
import app.mybad.utils.repeatWeekAndDayOfMonth
import app.mybad.utils.systemToEpochSecond
import app.mybad.utils.timeInMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getPatternUsagesWithParamsBetweenUseCase: GetPatternUsagesWithParamsBetweenUseCase,
    private val getUsagesWithParamsBetweenUseCase: GetUsagesWithParamsBetweenUseCase,
    private val setFactUseTimeOrInsertUsageUseCase: SetFactUseTimeOrInsertUsageUseCase,
) : BaseViewModel<CalendarContract.Event, CalendarContract.State, CalendarContract.Effect>() {

    override fun setInitialState() = CalendarContract.State()

    override fun handleEvents(event: CalendarContract.Event) {
        when (event) {
            is CalendarContract.Event.ActionBack -> {
                setEffect { CalendarContract.Effect.Navigation.Back }
            }

            is CalendarContract.Event.ChangeMonth -> changeDateForMonth(event.date)

            is CalendarContract.Event.SelectDate -> changeDateForDaily(event.date)

            is CalendarContract.Event.SetUsage -> setUsagesFactTime(event.usage)

            is CalendarContract.Event.SelectElement -> selectElement(event.element)
        }
    }

    private val dateMonth = MutableStateFlow(viewState.value.date)
    private var patternsMonth: List<UsageDisplayDomainModel> = emptyList()
    private var usagesMonth: List<UsageDisplayDomainModel> = emptyList()

    @OptIn(ExperimentalCoroutinesApi::class)
    var dateUpdate = dateMonth.flatMapLatest { date ->
        // пересчитываем и перезагружаем значениея для каждого месяца,
        // дата меняется только при изменении месяца
        val firstDayWeekOfMonth = date.minusDays(date.dayOfWeek.ordinal)
        // определим начало недели для отображения 6 недель, время с учетом часового пояса, т.е 00:00 и 23:59
        val dateMin = firstDayWeekOfMonth.atStartOfDay().systemToEpochSecond()
        // определим последний день 6 недель
        val dateMax = firstDayWeekOfMonth.plusDays(WEEKS_PER_MONTH * DAYS_A_WEEK - 1)
            .atEndOfDay().systemToEpochSecond()

        Log.w(
            "VTTAG",
            "CalendarViewModel::changeDateForMonth: date=$date start=${dateMin.displayDateTimeUTC()} end=${dateMax.displayDateTimeUTC()}" // UTC
        )

        combine(
            getPatternUsagesWithParamsBetweenUseCase(
                startTime = dateMin,
                endTime = dateMax,
            ),
            getUsagesWithParamsBetweenUseCase(
                startTime = dateMin,
                endTime = dateMax,
            )
        ) { patterns, usages ->
            patternsMonth = patterns
            usagesMonth = usages
            Log.w(
                "VTTAG",
                "CalendarViewModel::changeDateForMonth: patternsMonth=${patternsMonth.size} usagesMonth=${usagesMonth.size}"
            )

            calculateByWeekAndSetState(date)
        }

    }

    private fun changeDateForMonth(date: LocalDateTime) {
        viewModelScope.launch {
            dateMonth.value = date.firstDayOfMonth()
        }
    }

    private fun calculateByWeekAndSetState(date: LocalDateTime): LocalDateTime {
        Log.w(
            "VTTAG",
            "CalendarViewModel::calculateByWeekAndSetState: in"
        )
        date.repeatWeekAndDayOfMonth { week, day, dateTime ->
            viewState.value.datesWeeks[week][day] = dateTime
            viewState.value.usagesWeeks[week][day] = getUsagesOnDate(dateTime)
        }
        // текущаая дата для отображения на календаре и обновление данных
        val dateUpdate = currentDateTimeSystem()
        setState {
            copy(
                date = date,
                updateDateWeek = dateUpdate,// для изменения стейта, datesWeeks и usagesWeeks, не меняют стейт
                datesWeeks = viewState.value.datesWeeks,
                usagesWeeks = viewState.value.usagesWeeks,
            )
        }
        return dateUpdate
    }

    private fun getUsagesOnDate(date: LocalDateTime): List<UsageDisplayDomainModel> {
        val pattens = patternsMonth.filter { it.checkDate(date) }
            .map { pattern ->
                val useTime = date.changeTime(minutes = pattern.timeInMinutes)
                pattern.copy(
                    name = "${pattern.name}|P|${useTime.displayDateTimeShort()}",
                    useTime = useTime,
                    timeInMinutes = useTime.timeInMinutes()
                )
            }.associateBy { it.toUsageKey() }
        val usages = usagesMonth.filter { it.checkDate(date) }
            .map { usage ->
                usage.copy(
                    name = "${usage.name}|U|${usage.useTime.displayDateTimeShort()}",
                )
            }.associateBy { it.toUsageKey() }
        val pattensAndUsages = pattens.plus(usages).values.toList()
        Log.w(
            "VTTAG",
            "CalendarViewModel::changeDateForMonth: date=${date.displayDateTime()} ${pattensAndUsages.size}=[pattens=${pattens.size}, usages=${usages.size}]"
        )
        return pattensAndUsages
    }

    private fun selectElement(element: Pair<Int, Int>?) {
        viewModelScope.launch {
            Log.w("VTTAG", "CalendarViewModel::selectElement: element=${element}")
            val elementOrNull = try {
                if (element != null && viewState.value.usagesWeeks[element.first][element.second].isNotEmpty()) {
                    element
                } else null
            } catch (_: Error) {
                null
            }
            setState {
                copy(
                    selectedElement = elementOrNull,
                )
            }
        }
    }

    private fun changeDateForDaily(date: LocalDateTime?) {
        viewModelScope.launch {
            date?.let {
                // TODO("алгоритм пересчета недели")
//                val week = date.dayOfMonth % WEEKS_PER_MONTH // так не работает, у нас меняется месяц
                val day = date.dayOfWeek.ordinal // день определяется
                // перебор по всем неделям
                repeat(WEEKS_PER_MONTH) { week ->
                    if (date == viewState.value.datesWeeks[week][day]) {
                        Log.w(
                            "VTTAG",
                            "CalendarViewModel::changeDateForDaily: date=${date.displayDateTime()} selectElement=$week, $day"
                        )
                        selectElement(week to day)
                        return@launch
                    }
                }
            }
            selectElement(null)
        }
    }

    private fun setUsagesFactTime(usage: UsageDisplayDomainModel) {
        viewModelScope.launch {
            Log.w(
                "VTTAG",
                "CalendarViewModel::setUsagesFactTime: pattern or usage id=${usage.id}: ${usage.factUseTime}"
            )
            val dateTime = currentDateTimeSystem()
            // тут отправка с проверкой на дублирование
            setFactUseTimeOrInsertUsageUseCase(
                usageDisplay = usage,
                currentDateTime = dateTime, // local
                currentDateTimeUTC = dateTime.systemToEpochSecond(), // UTC
                useTimeUTC = usage.useTime.systemToEpochSecond(), // UTC
            ).onFailure {
                TODO("setUsagesFactTime: сделать обработку ошибок")
            }
        }
    }

}
