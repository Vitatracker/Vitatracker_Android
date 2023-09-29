package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.domain.usecases.courses.GetCoursesUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.betweenDays
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.isEqualsDay
import app.mybad.utils.plusDays
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

    private val currentDateTime = MutableStateFlow(currentDateTimeSystem()) // с учетом часового пояса

    init {
        Log.w("VTTAG", "MyCoursesViewModel: init")
        uploadCoursesInState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun uploadCoursesInState() {
        viewModelScope.launch {
            currentDateTime.flatMapLatest {
                getCoursesUseCase()
                    .distinctUntilChanged()
                    .map(::addRemindCourse)
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
        val currentDate = currentDateTimeSystem()
        val newCourses = mutableListOf<CourseDisplayDomainModel>()
        return courses.mapNotNull { course ->
            course.remindDate?.let {
                val startDate = course.endDate.plusDays(course.interval).atStartOfDay()
                if (startDate > currentDate) {
                    val endDate = startDate.plusDays(
                        // проверим что курс на 1 день
                        if (course.endDate.isEqualsDay(course.startDate)) 0
                        else course.endDate.atStartOfDay().betweenDays(course.startDate)
                    ).atEndOfDay()
                    newCourses.add(
                        course.copy(
                            id = 1000000 + course.id,
                            idn = 0,

                            idOld = if (course.endDate >= currentDate) 0 else course.id, // для редактирования

                            startDate = startDate,
                            endDate = endDate,
                            remindDate = null,
                            interval = startDate.betweenDays(currentDate.atEndOfDay()), // старт курса через ... дней
                        )
                    )
                }
            }
            // проверяем, что курс уже закончен, не показываем его, но для редактирования id у нас прописан
            if (course.endDate >= currentDate) course else null
        }.plus(newCourses)
    }

}
