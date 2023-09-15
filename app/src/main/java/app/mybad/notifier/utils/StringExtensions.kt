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

// quantity
fun Float.toText() = if (this % 1 == 0.0f) {
    "%d".format(this.toInt())
} else {
    "%,.3f".format(this).takeIf { it.last() != '0' } ?: "%,.2f".format(this)
}

fun Pair<Float, Float>.toText() = if (this.first == this.second) this.second.toText()
else "${this.first.toText()}-${this.second.toText()}"

fun Int.toText() = "%02d".format(this)
