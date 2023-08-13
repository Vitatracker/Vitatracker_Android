package app.mybad.notifier.utils

private val passRegex = "^(?=.{6,16})(?=.*[a-z])(?=.*[A-Z])(?=.*[~@#$%^&+*!=]).*$".toRegex()
private val emailRegex ="""^(?=[a-z0-9][a-z0-9@._%+-]{5,253}$)([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,63}$""".toRegex()

fun String.isValidEmail() = if (this.isNotBlank()) emailRegex.matches(this.lowercase())
else false

fun String.isValidPassword() = if (this.isNotBlank()) passRegex.matches(this)
else false
