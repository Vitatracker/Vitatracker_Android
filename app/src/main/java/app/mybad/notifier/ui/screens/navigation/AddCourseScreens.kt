package app.mybad.notifier.ui.screens.navigation

import app.mybad.domain.models.med.MedDomainModel
import com.google.gson.Gson

sealed class AddCourseScreens(val route: String) {

    object FirstScreen : AddCourseScreens(ROUTE_FIRST)
    object SecondScreen : AddCourseScreens(ROUTE_SECOND) {
        private const val ROUTE_FOR_ARGS = "add_course_second"

        fun getRouteWithArgs(medDomainModel: MedDomainModel): String {
            return "$ROUTE_FOR_ARGS/${Gson().toJson(medDomainModel).toString().encode()}"
        }
    }

    object ThirdScreen : AddCourseScreens(ROUTE_THIRD) {
        private const val ROUTE_FOR_ARGS = "add_course_third"

        fun getRouteWithArgs(medDomainModel: MedDomainModel): String {
            return "$ROUTE_FOR_ARGS/${Gson().toJson(medDomainModel).toString().encode()}"
        }
    }

    companion object {
        const val KEY_MED = "med"
        private const val ROUTE_FIRST = "add_course_first"
        private const val ROUTE_SECOND = "add_course_second/{${KEY_MED}}"
        private const val ROUTE_THIRD = "add_course_third/{${KEY_MED}}"
    }
}