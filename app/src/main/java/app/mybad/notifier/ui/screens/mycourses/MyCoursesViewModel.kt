package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.data.models.MyCoursesState
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.usecases.courses.DeleteCourseUseCase
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.domain.usecases.courses.UpdateCourseUseCase
import app.mybad.domain.usecases.meds.UpdateMedUseCase
import app.mybad.domain.usecases.usages.UpdateAllUsagesInCourseUseCase
import app.mybad.network.models.AuthToken
import app.mybad.network.repos.repo.CoursesNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    loadCourses: LoadCoursesUseCase,

    private val deleteCourse: DeleteCourseUseCase,
    private val updateCourse: UpdateCourseUseCase,
    private val updateMed: UpdateMedUseCase,

    private val coursesRepo: CoursesRepo,
    private val usagesRepo: UsagesRepo,

    private val updateUsagesInCourse: UpdateAllUsagesInCourseUseCase,

    private val coursesNetworkRepo: CoursesNetworkRepo,
) : ViewModel() {

    val state = loadCourses(AuthToken.userId)
        .mapLatest { (courses, meds, usages) ->
            Log.w("VTTAG", "MyCoursesViewModel::state: ok")
            MyCoursesState(courses = courses, meds = meds, usages = usages)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = MyCoursesState()
        )

    fun reduce(intent: MyCoursesIntent) {
        when (intent) {
            is MyCoursesIntent.Delete -> {
                viewModelScope.launch {
                    val mId = coursesRepo.getSingle(intent.courseId).medId
                    deleteCourse.execute(intent.courseId)
                    coursesNetworkRepo.deleteMed(mId)
                }
            }

            is MyCoursesIntent.Update -> {
                viewModelScope.launch {
                    updateMed(intent.med)
                    updateCourse.execute(intent.course.id, intent.course)
                    updateUsagesInCourse(intent.usagesPattern, intent.med, intent.course)
                    coursesNetworkRepo.updateAll(
                        med = intent.med,
                        course = intent.course,
                        usages = usagesRepo.getUsagesByMedId(intent.med.id)
                    )
                }
            }
        }
    }
}
