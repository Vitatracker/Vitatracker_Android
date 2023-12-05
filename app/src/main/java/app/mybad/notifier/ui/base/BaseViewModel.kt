package app.mybad.notifier.ui.base

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface ViewEvent

interface ViewState

interface ViewSideEffect

const val SIDE_EFFECTS_KEY = "side-effects_key"

abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect> :
    ViewModel() {

    abstract fun setInitialState(): UiState
    abstract fun handleEvents(event: Event)

    private val initialState: UiState by lazy { setInitialState() }

    private val _viewState: MutableState<UiState> = mutableStateOf(initialState)
    val viewState: State<UiState> = _viewState

    private val _event = MutableSharedFlow<Event>()
    private var eventOld: Event? = null

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    fun setEvent(event: Event) {
        viewModelScope.launch {
            if (event == eventOld) return@launch
            eventOld = event
            _event.emit(event)
            delay(1000)
            eventOld = null
        }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
        Log.d("VTTAG", "BaseViewModel::setState: state=${newState}")
//        Log.w("VTTAG", "BaseViewModel::setState: state=${viewState.value}")
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        Log.w("VTTAG", "BaseViewModel::setEffect: effect=$effectValue")
        viewModelScope.launch { _effect.send(effectValue) }
    }

    override fun onCleared() {
        _effect.close()
        super.onCleared()
    }
}
