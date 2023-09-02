package app.mybad.notifier.ui.screens.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.remedies.GetRemediesByListIdUseCase
import app.mybad.domain.usecases.usages.GetUsagesBetweenUseCase
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.toEpochSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRemediesByListIdUseCase: GetRemediesByListIdUseCase,
    private val getUsagesBetweenUseCase: GetUsagesBetweenUseCase,
    private val updateUsageUseCase: UpdateUsageUseCase,
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    override fun setInitialState() = MainContract.State()

    override fun handleEvents(event: MainContract.Event) {
        Log.d("VTTAG", "MainViewModel::handleEvents: event=$event")
        when (event) {
            is MainContract.Event.ChangeDate -> changeData(event.date)
            is MainContract.Event.SetUsageFactTime -> setUsagesFactTime(event.usageId)
        }
    }

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

    private val startAndEndDate = MutableSharedFlow<LocalDateTime>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val usages = startAndEndDate.flatMapLatest { date ->
        Log.w("VTTAG", "MainViewModel::receivingCourses: usages date=$date")
        getUsagesBetweenUseCase(
            date.atStartOfDay().toEpochSecond(),
            date.atEndOfDay().toEpochSecond(),
        )
    }
        .distinctUntilChanged()
        .onEach {
            Log.w("VTTAG", "MainViewModel::receivingCourses: usages=${it.size}")
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val remedies = usages.flatMapLatest { usages ->
        Log.w("VTTAG", "MainViewModel::receivingCourses: remedies")
        val remedyIds = usages.map { usage ->
            usage.remedyId
        }.toSet().toList()
        getRemediesByListIdUseCase(remedyIds)
    }
        .distinctUntilChanged()
        .onEach {
            Log.w("VTTAG", "MainViewModel::receivingCourses: remedies=${it.size}")
        }

    init {
        Log.w("VTTAG", "MainViewModel::receivingCourses: init")
        receivingCourses()
    }

    private fun receivingCourses() {
        viewModelScope.launch {
            combine(
                remedies,
                usages,
                ::Pair
            ).collect {
                Log.w(
                    "VTTAG",
                    "MainViewModel::receivingCourses: combine usages=${it.second.size} remedies=${it.first.size}"
                )
                setState {
                    copy(
                        remedies = it.first,
                        usages = it.second,
                    )
                }
            }
        }
    }

    private fun changeData(date: LocalDateTime) {
        viewModelScope.launch {
            setState {
                copy(date = date)
            }
            startAndEndDate.emit(date)
            Log.d("VTTAG", "MainViewModel::changeData: date=$date")
        }
    }

    private fun setUsagesFactTime(usageId: Long) {
        viewModelScope.launch {
            viewState.value.usages.firstOrNull { it.id == usageId }?.let { usage ->
                updateUsageUseCase(
                    usage.copy(
                        factUseTime = if (usage.factUseTime <= 0) currentDateTimeInSecond() else -1,
                    )
                )
                // Синхронизируем с сервером
                AuthToken.requiredSetUsagesFactTime(usageId)
            }
        }
    }

}
