package app.mybad.domain.models

object AuthToken {
    var token = ""

    var refreshToken = ""

    var userId = 1L

    var email = ""

    fun clear() {
        token = ""
        refreshToken = ""
        userId = -1L
        email = ""
    }
}
