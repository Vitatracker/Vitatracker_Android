package app.mybad.notifier.utils

import android.util.Patterns

private val passRegex = "^(?=.{6,16})(?=.*[a-z])(?=.*[A-Z])(?=.*[~@#$%^&+*!=.()\\-`]).*$".toRegex()

fun String.isValidEmail() = if (this.isNotBlank())  Patterns.EMAIL_ADDRESS.matcher(this).matches()
else false

fun String.isValidPassword() = if (this.isNotBlank()) passRegex.matches(this)
else false
