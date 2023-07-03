package app.mybad.notifier.ui

import androidx.compose.ui.graphics.Color
import app.mybad.notifier.ui.theme.selectColorGreen
import app.mybad.notifier.ui.theme.selectColorOrange
import app.mybad.notifier.ui.theme.selectColorRed
import app.mybad.notifier.ui.theme.selectColorTurquoise
import app.mybad.notifier.ui.theme.selectColorViolet
import app.mybad.notifier.ui.theme.selectColorYellow

enum class PickColor(val color: Color) {
    PICK_COLOR_TURQUOISE(selectColorTurquoise),
    PICK_COLOR_VIOLET(selectColorViolet),
    PICK_COLOR_ORANGE(selectColorOrange),
    PICK_COLOR_GREEN(selectColorGreen),
    PICK_COLOR_YELLOW(selectColorYellow),
    PICK_COLOR_RED(selectColorRed);

    companion object {
        fun getColor(number: Int) = values()[number % values().lastIndex].color
    }
}
