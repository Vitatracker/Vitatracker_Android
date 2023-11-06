package app.mybad.notifier.utils

private val passRegex =
    "^(?=.{6,16}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-!@#$%^&*()_+=\\[\\]{}|/<>?,.:;\"~']).*$".toRegex()

//"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-!@#$%^&*()_+=\\[\\]{}|/<>?,.:;\"~'])[A-Za-z\\d-!@#$%^&*()_+=\\[\\]{}|/<>?,.:;\"~']{6,16}$"
private val emailRegex =
    "^(?=.{6,254}$)(?=.{3,64}@)[A-Za-z0-9]{1,64}(?:[-_][A-Za-z0-9]+)*(?:\\.[A-Za-z0-9]+(?:[-_][A-Za-z0-9]+)*)*@(?=.{2,189}$)[A-Za-z0-9]+(?:[-_][A-Za-z0-9]{2,63}+)*(?:\\.[A-Za-z0-9]{2,63}(?:[-_][A-Za-z0-9]{2,63}+)*){0,}(?:\\.[A-Za-z0-9]{2,63}(?:[-_][A-Za-z0-9]{2,63}+)*)?$".toRegex()
//"^(?=.{6,254}$)(?=.{3,64}@)[A-Za-z0-9]{1,64}(?:[-_][A-Za-z0-9]+)*(?:\\.[A-Za-z0-9]+(?:[-_][A-Za-z0-9]+)*)*@(?=.{2,189}$)[A-Za-z0-9]+(?:[-_][A-Za-z0-9]{2,63}+)*(?:\\.[A-Za-z0-9]{2,63}(?:[-_][A-Za-z0-9]{2,63}+)*){0,}(?:\\.[A-Za-z0-9]{2,63}(?:[-_][A-Za-z0-9]{2,63}+)*)?$"

fun String.isValidEmail() = if (this.isNotBlank()) emailRegex.matches(this)
else false

fun String.isValidPassword() = if (this.isNotBlank()) passRegex.matches(this)
else false
