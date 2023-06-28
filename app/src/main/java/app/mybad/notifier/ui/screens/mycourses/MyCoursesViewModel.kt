package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.data.models.MyCoursesState
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.courses.DeleteCourseUseCase
import app.mybad.domain.usecases.courses.GetCourseSingleUseCase
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.domain.usecases.courses.UpdateCourseAllUseCase
import app.mybad.domain.usecases.courses.UpdateCourseUseCase
import app.mybad.domain.usecases.meds.DeleteMedUseCase
import app.mybad.domain.usecases.meds.UpdateMedUseCase
import app.mybad.domain.usecases.usages.GetUsagesByMedIdUseCase
import app.mybad.domain.usecases.usages.UpdateUsagesInCourseUseCase
import app.mybad.notifier.ui.screens.common.generateCommonUsages
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.toEpochSecond
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
    private val updateCourseAll: UpdateCourseAllUseCase,

    private val updateMed: UpdateMedUseCase,
    private val deleteMed: DeleteMedUseCase,

    private val updateUsagesInCourse: UpdateUsagesInCourseUseCase,

    private val getCourseSingleUseCase: GetCourseSingleUseCase,
    private val getUsagesByMedIdUseCase: GetUsagesByMedIdUseCase,
) : ViewModel() {

    val state = loadCourses(AuthToken.userId)
        .mapLatest { (courses, meds, usages) ->
            Log.w("VTTAG", "MyCoursesViewModel::state: meds=${meds.size} usages=${usages.size}")
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
                    val mId = getCourseSingleUseCase(intent.courseId).medId
                    deleteCourse.execute(intent.courseId, getCurrentDateTime().toEpochSecond())
                    deleteMed(mId)
                }
            }

            is MyCoursesIntent.Update -> {
                viewModelScope.launch {
                    updateMed(intent.med)
                    updateCourse.execute(intent.course.id, intent.course)
                    Log.w(
                        "VTTAG",
                        "MyCoursesViewModel::Update: userId=${intent.med.userId} pattern=${intent.usagesPattern} "
                    )
                    updateUsagesInCourse(
                        usages = generateCommonUsages(
                            usagesByDay = intent.usagesPattern,
                            medId = intent.med.id,
                            userId = intent.med.userId,
                            startDate = intent.course.startDate,
                            endDate = intent.course.endDate,
                            regime = intent.course.regime
                        )
                    )
                    updateCourseAll(
                        med = intent.med,
                        course = intent.course,
                        usages = getUsagesByMedIdUseCase(intent.med.id)
                    )
                }
            }
        }
    }
}
