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
import app.mybad.utils.currentDateTime
import app.mybad.utils.toDateTimeShortDisplay
import app.mybad.utils.toEpochSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
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
        Log.w("VTTAG", "MainViewModel::receivingCourses: pattern + usages date=$date")
        combine(
            getPatternUsagesWithNameAndDateBetweenUseCase(
                startTime = date.atStartOfDay().toEpochSecond(),
                endTime = date.atEndOfDay().toEpochSecond(),
            ),
            getUsagesWithNameAndDateBetweenUseCase(
                startTime = date.atStartOfDay().toEpochSecond(),
                endTime = date.atEndOfDay().toEpochSecond(),
            ),
        ) { p, u ->
//            p.plus(u).toSortedMap()
            // для тестов, потом удалить
            p.plus(u).mapValues {
                val pattern = it.value
                pattern.copy(name = "${pattern.name}|${if (pattern.isPattern) "P" else "U"}|${pattern.useTime.toDateTimeShortDisplay()}")
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
                val dateTime = currentDateTime()
                // тут отправка с проверкой на дублирование
                setFactUseTimeOrInsertUsageUseCase(
                    usageDisplay = usageDisplay,
                    currentDateTime = dateTime.toEpochSecond(),
                ).onFailure {
                    TODO("setUsagesFactTime: сделать обработку ошибок")
                }
            }
        }
    }

}
