package app.mybad.domain.models

object AuthToken {
    var token = ""

    var userId = -1L

    var email = ""

    fun clear() {
        token = ""
        userId = -1L
        email = ""
    }
}
