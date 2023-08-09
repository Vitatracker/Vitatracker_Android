package app.mybad.domain.models

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthToken {
    private val _isAuthorize = MutableStateFlow(false)
    val isAuthorize = _isAuthorize.asStateFlow()

    var userId = -1L

    var email = ""

    var token = ""
        set(value) {
            field = value
            _isAuthorize.value = value.isNotBlank() && userId != -1L
            Log.w("VTTAG", "AuthToken::token: ${_isAuthorize.value} userId=${userId} token=$value")
        }
    var tokenDate = 0L

    var tokenRefresh = ""
    var tokenRefreshDate = 0L

    fun clear() {
        userId = -1L
        tokenDate = 0L
        tokenRefresh = ""
        tokenRefreshDate = 0L
        email = ""
        token = ""
    }
}
