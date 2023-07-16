package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.data.models.MyCoursesState
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.courses.DeleteCourseFullUseCase
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.domain.usecases.courses.UpdateCourseUseCase
import app.mybad.domain.usecases.remedies.UpdateRemedyUseCase
import app.mybad.domain.usecases.usages.UpdateUsagesInCourseUseCase
import app.mybad.notifier.ui.screens.common.generateUsages
import app.mybad.theme.utils.getCurrentDateTime
import app.mybad.theme.utils.toEpochSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    loadCourses: LoadCoursesUseCase,

    private val updateCourseUseCase: UpdateCourseUseCase,
    private val deleteCourseFullUseCase: DeleteCourseFullUseCase,

    private val updateRemedyUseCase: UpdateRemedyUseCase,

    private val updateUsagesInCourseUseCase: UpdateUsagesInCourseUseCase,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = loadCourses(AuthToken.userId)
        .mapLatest { (courses, remedies, usages) ->
            Log.w("VTTAG", "MyCoursesViewModel::state: meds=${remedies.size} usages=${usages.size}")
            MyCoursesState(courses = courses, remedies = remedies, usages = usages)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = MyCoursesState()
        )

    fun reduce(intent: MyCoursesIntent) {
        when (intent) {
            is MyCoursesIntent.Delete -> {
                viewModelScope.launch {
                    deleteCourseFullUseCase(intent.courseId, getCurrentDateTime().toEpochSecond())
                    //TODO("запустить воркер удаления курса на беке")
                }
            }

            is MyCoursesIntent.Update -> {
                viewModelScope.launch {
                    updateRemedyUseCase(intent.remedy)
                    updateCourseUseCase(intent.course)
                    Log.w(
                        "VTTAG",
                        "MyCoursesViewModel::Update: userId=${intent.course.userId} pattern=${intent.usagesPattern} "
                    )
                    updateUsagesInCourseUseCase(
                        usages = generateUsages(
                            usagesByDay = intent.usagesPattern,
                            courseId = intent.course.id,
                            userId = intent.course.userId,
                            startDate = intent.course.startDate,
                            endDate = intent.course.endDate,
                            regime = intent.course.regime
                        )
                    )
                    //TODO("запустить воркер обновления на беке")
                }
            }
        }
    }
}
