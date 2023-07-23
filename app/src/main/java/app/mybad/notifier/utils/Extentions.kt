package app.mybad.notifier.utils

import android.util.Patterns

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return Regex("^(?=.{6,16})(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+*!=]).*\$").matches(this)
}