package app.mybad.notifier.ui.screens.calender

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.usecases.usages.GetPatternUsagesWithParamsBetweenUseCase
import app.mybad.domain.usecases.usages.GetUsagesWithParamsBetweenUseCase
import app.mybad.domain.usecases.usages.SetFactUseTimeOrInsertUsageUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.DAYS_A_WEEK
import app.mybad.utils.SECONDS_IN_DAY
import app.mybad.utils.WEEKS_PER_MONTH
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.atStartOfMonth
import app.mybad.utils.changeTimeOfSystem
import app.mybad.utils.currentDateTime
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.minusDays
import app.mybad.utils.plusDays
import app.mybad.utils.toDateDisplay
import app.mybad.utils.toDateTimeDisplay
import app.mybad.utils.toDateTimeShortDisplay
import app.mybad.utils.toEpochSecond
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
    private var patternsMonth: Map<String, UsageDisplayDomainModel> = emptyMap()
    private var usagesMonth: Map<String, UsageDisplayDomainModel> = emptyMap()

    @OptIn(ExperimentalCoroutinesApi::class)
    var dateUpdate = dateMonth.flatMapLatest { date ->
        // пересчитываем и перезагружаем значениея для каждого месяца,
        // дата меняется только при изменении месяца
        val startOfMonth = date.atStartOfMonth()
        // определим начало недели для отображения 6 недель
        val startDayOfWeek = startOfMonth.minusDays(startOfMonth.dayOfWeek.ordinal)
        val dateMin = startDayOfWeek.atStartOfDay().toEpochSecond()
        // определим последний день 6 недель
        val dateMax = startDayOfWeek.plusDays(WEEKS_PER_MONTH * DAYS_A_WEEK - 1)
            .atEndOfDay().toEpochSecond()

        Log.w(
            "VTTAG",
            "CalendarViewModel::changeDateForMonth: date=$date start=${dateMin.toDateDisplay()} end=${dateMax.toDateDisplay()}"
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
            patternsMonth = patterns.toSortedMap()
            usagesMonth = usages.toSortedMap()
            Log.w(
                "VTTAG",
                "CalendarViewModel::changeDateForMonth: patternsMonth=${patternsMonth.size} usagesMonth=${usagesMonth.size}"
            )

            calculateByWeekAndSetState(date)
        }

    }

    private fun changeDateForMonth(date: LocalDateTime) {
        viewModelScope.launch {
            dateMonth.value = date
        }
    }

    private fun calculateByWeekAndSetState(date: LocalDateTime): LocalDateTime {
        Log.w(
            "VTTAG",
            "CalendarViewModel::calculateByWeekAndSetState: in"
        )
        val datesWeeks: Array<Array<LocalDateTime?>> = Array(WEEKS_PER_MONTH) {
            Array(DAYS_A_WEEK) { null }
        }
        val usagesWeeks: Array<Array<List<UsageDisplayDomainModel>>> = Array(WEEKS_PER_MONTH) {
            Array(DAYS_A_WEEK) { emptyList() }
        }

        // начало месяца и от этого дня берется неделя и первый день недели
        val fwd = date.atStartOfMonth()
        repeat(WEEKS_PER_MONTH) { week ->
            repeat(DAYS_A_WEEK) { day ->
                val time = if (week == 0 && day < fwd.dayOfWeek.value) {
                    fwd.minusDays(fwd.dayOfWeek.ordinal - day)
                } else {
                    fwd.plusDays(week * DAYS_A_WEEK - fwd.dayOfWeek.ordinal + day)
                }
                datesWeeks[week][day] = time
                usagesWeeks[week][day] = getUsagesOnDate(time)
            }
        }
        val dateUpdate =
            currentDateTime() // текущаая дата для отображения на календаре и обновление данных
        setState {
            copy(
                date = date,
                updateDateWeek = dateUpdate,// для изменения стейта, datesWeeks и usagesWeeks, не меняют стейт
                datesWeeks = datesWeeks,
                usagesWeeks = usagesWeeks,
            )
        }
        return dateUpdate
    }

    private fun getUsagesOnDate(date: LocalDateTime): List<UsageDisplayDomainModel> {
        val pattens = patternsMonth.filter { it.value.checkDate(date) }
        val usages = usagesMonth.filter { it.value.checkDate(date) }
        val pattensAndUsages = pattens.plus(usages).values.map {
/*
            // рабочий вариант
            if (it.isPattern) {
                // формируем на основе паттерна и даты для выборки - usage.useTime
                it.copy(useTime = date.changeTimeOfSystem(minutes =  it.timeInMinutes))
            } else it
*/
            // для теста, потом удалить
            it.copy(
                name = "${it.name}|${if (it.isPattern) "P" else "U"}|${
                    date.toEpochSecond().changeTimeOfSystem(minutes = it.timeInMinutes)
                        .toDateTimeShortDisplay()
                }",
                // формируем на основе паттерна и даты для выборки или остается прежняя
                useTime = if (it.isPattern) date.changeTimeOfSystem(minutes = it.timeInMinutes) else it.useTime,
            )
        }
        Log.w(
            "VTTAG",
            "CalendarViewModel::changeDateForMonth: date=${date.toDateTimeDisplay()} ${pattensAndUsages.size}=[pattens=${pattens.size}, usages=${usages.size}]"
        )
        return pattensAndUsages
    }

    private fun UsageDisplayDomainModel.checkDate(date: LocalDateTime): Boolean {
        return if (this.isPattern) {
            val time = date.toEpochSecond()
            time in this.startDate..this.startDate.atEndOfDay() ||
                ((this.regime == 0 || ((time - this.startDate) / SECONDS_IN_DAY) % (this.regime + 1) == 0L) &&
                    time in this.startDate..this.endDate)
        } else {
            val start = date.atStartOfDay().toEpochSecond()
            val end = date.atEndOfDay().toEpochSecond()
            this.useTime in start..end
        }
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
                            "CalendarViewModel::changeDateForDaily: date=${date.toDateTimeDisplay()} selectElement=$week, $day"
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
            // тут отправка с проверкой на дублирование
            setFactUseTimeOrInsertUsageUseCase(
                usageDisplay = usage,
                currentDateTime = currentDateTimeInSecond(),
            ).onFailure {
                TODO("setUsagesFactTime: сделать обработку ошибок")
            }
        }
    }

}
