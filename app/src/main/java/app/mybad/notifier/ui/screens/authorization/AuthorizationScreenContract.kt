package app.mybad.notifier.ui.screens.authorization

data class AuthorizationScreenContract(
    private val login: String = "",
    private val password: String = "",
    private val error: String = "",
    private val exception: String = ""
)
