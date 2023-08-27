package app.mybad.notifier.utils

import android.util.Patterns
import app.mybad.theme.R

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return Regex("^(?=.{6,16})(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+*!=]).*\$").matches(this)
}

fun getFormsPluralsArray(): Array<Int> = arrayOf(
    R.plurals.plurals_types_tablet,
    R.plurals.plurals_types_pill,
    R.plurals.plurals_types_inhalator,
    R.plurals.plurals_types_injection,
    R.plurals.plurals_types_drops,
    R.plurals.plurals_types_solution,
    R.plurals.plurals_types_suppository,
    R.plurals.plurals_types_syrup,
    R.plurals.plurals_types_spray,
    R.plurals.plurals_types_suspension
)