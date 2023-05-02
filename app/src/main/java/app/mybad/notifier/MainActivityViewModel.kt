package app.mybad.notifier

import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _uiState = MutableStateFlow(MainActivityContract())
    val uiState = _uiState.asStateFlow()

    init {
        updateToken()
    }

    fun updateToken() {
        scope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    token = dataStoreRepo.getToken().first()
                )
            )
        }
    }

    fun clearToken() {
        scope.launch {
            dataStoreRepo.updateToken("")
            updateToken()
        }
    }

}
