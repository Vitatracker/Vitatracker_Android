package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.domain.usecases.courses.GetCoursesUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.plusDay
import app.mybad.utils.secondsToDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
) : BaseViewModel<MyCoursesContract.Event, MyCoursesContract.State, MyCoursesContract.Effect>() {


    override fun setInitialState() = MyCoursesContract.State()

    override fun handleEvents(event: MyCoursesContract.Event) {
        viewModelScope.launch {
            when (event) {
                is MyCoursesContract.Event.CourseEditing -> {
                    setEffect { MyCoursesContract.Effect.Navigation.ToCourseEditing(event.courseId) }
                }

                MyCoursesContract.Event.ActionBack -> setEffect { MyCoursesContract.Effect.Navigation.Back }
            }
        }
    }

    private val currentDateTime = MutableStateFlow(currentDateTimeInSecond())

    init {
        Log.w("VTTAG", "MyCoursesViewModel: init")
        uploadCoursesInState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun uploadCoursesInState() {
        viewModelScope.launch {
            currentDateTime.flatMapLatest { date ->
                getCoursesUseCase()
                    .distinctUntilChanged()
                    .map { courses ->
                        addRemindCourse(courses)
                    }
            }
                .distinctUntilChanged()
                .collect(::changeState)
        }
    }

    private fun changeState(courses: List<CourseDisplayDomainModel>) {
        viewModelScope.launch {
            setState {
                copy(
                    courses = courses,
                )
            }
        }
    }

    private fun addRemindCourse(courses: List<CourseDisplayDomainModel>): List<CourseDisplayDomainModel> {
        val currentDate = currentDateTimeInSecond()
        val newCourses = mutableListOf<CourseDisplayDomainModel>()
        courses.forEach { newCourse ->
            if (newCourse.remindDate > 0) {

                val startDate = newCourse.endDate.plusDay(newCourse.interval).atStartOfDay()
                if (startDate > currentDate) {
                    val endDate = startDate.plusDay(
                        (newCourse.endDate.atStartOfDay() - newCourse.startDate).secondsToDay()
                    ).atEndOfDay()
                    newCourses.add(
                        newCourse.copy(
                            idn = 0,
                            startDate = startDate,
                            endDate = endDate,
                            remindDate = 0,
                            interval = (startDate - currentDate.atStartOfDay()).secondsToDay(), // старт курса через ... дней
                        )
                    )
                }
            }
        }
        return courses.plus(newCourses)
    }

}
