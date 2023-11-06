package app.mybad.utils

// quantity
fun Float.toText() = if (this % 1 == 0.0f) {
    "%d".format(this.toInt())
} else {
    "%.4f".format(this).takeIf { it.last() != '0' }
        ?: "%.3f".format(this).takeIf { it.last() != '0' }
        ?: "%.2f".format(this).takeIf { it.last() != '0' }
        ?: "%.1f".format(this).takeIf { it.last() != '0' }
        ?: "%d".format(this.toInt())
}

fun Pair<Float, Float>.toText() = if (this.first == this.second) this.second.toText()
else "${this.first.toText()}-${this.second.toText()}"

fun Int.toText() = "%02d".format(this)
