package app.mybad.notifier.ui.theme

import androidx.compose.ui.graphics.Color

enum class PickColor(val color: Color) {
    PICK_COLOR_TURQUOISE(selectColorTurquoise),
    PICK_COLOR_VIOLET(selectColorViolet),
    PICK_COLOR_ORANGE(selectColorOrange),
    PICK_COLOR_GREEN(selectColorGreen),
    PICK_COLOR_YELLOW(selectColorYellow),
    PICK_COLOR_RED(selectColorRed);

    companion object {
        fun getColor(number: Int) = entries[number % entries.lastIndex].color
    }
}
