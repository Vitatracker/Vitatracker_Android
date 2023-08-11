package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import app.mybad.data.models.MyCoursesState
import app.mybad.data.repos.SynchronizationCourseWorker.Companion.start
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.usecases.courses.CloseCourseUseCase
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.courses.DeleteCourseFullUseCase
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.domain.usecases.courses.UpdateCourseUseCase
import app.mybad.domain.usecases.remedies.CreateRemedyUseCase
import app.mybad.domain.usecases.remedies.GetRemedyByIdUseCase
import app.mybad.domain.usecases.remedies.UpdateRemedyUseCase
import app.mybad.domain.usecases.usages.GetUseUsagesInCourseUseCase
import app.mybad.domain.usecases.usages.UpdateUsagesInCourseUseCase
import app.mybad.notifier.ui.screens.common.generateUsages
import app.mybad.utils.currentDateTimeInSecond
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

    private val getRemedyByIdUseCase: GetRemedyByIdUseCase,
    private val createRemedyUseCase: CreateRemedyUseCase,
    private val updateRemedyUseCase: UpdateRemedyUseCase,

    private val createCourseUseCase: CreateCourseUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase,
    private val closeCourseUseCase: CloseCourseUseCase,

    private val getUseUsagesInCourseUseCase: GetUseUsagesInCourseUseCase,
    private val updateUsagesInCourseUseCase: UpdateUsagesInCourseUseCase,

    private val deleteCourseFullUseCase: DeleteCourseFullUseCase,
    private val workSync: WorkManager,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = loadCourses(AuthToken.userId)
        .mapLatest { (courses, remedies, usages) ->
            Log.w(
                "VTTAG",
                "MyCoursesViewModel::state: remedies=${remedies.size} usages=${usages.size}"
            )
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
                    //TODO("проверить было ли использование")
                    deleteCourseFullUseCase(intent.courseId, currentDateTimeInSecond())
                    //TODO("запустить воркер удаления курса на беке")
                    workSync.start()
                }
            }

            is MyCoursesIntent.Update -> {
                viewModelScope.launch {
                    //TODO("тут необходимо переделать, закрывать старый курс и пересоздавать курс с новой датой")
                    // проверить есть ли использование таблетки
                    val isUseUsage = getUseUsagesInCourseUseCase(intent.course.id)
                        .getOrNull() ?: false
                    if (isUseUsage) {
                        // таблетки уже были выпиты, закрыть с текущай датой курс и создать новый
                        createNewCourse(intent)
                    } else {
                        // курс не использовался, можно изменять без последствий
                        updateCourse(intent)
                    }
                    //TODO("запустить воркер обновления на беке")
                    workSync.start()
                }
            }
        }
    }

    private suspend fun createNewCourse(intent: MyCoursesIntent.Update) {
        val dateNew = currentDateTimeInSecond()
        // сравнить таблетку и создать новую если нужно
        val remedyId = getRemedyByIdUseCase(intent.remedy.id).getOrNull()
            ?.takeIf { it != intent.remedy }?.let {
                val remedy = RemedyDomainModel(
                    id = 0,
                    createdDate = dateNew,
                    userId = intent.remedy.userId,
                    userIdn = intent.remedy.userIdn,
                    name = intent.remedy.name,
                    description = intent.remedy.description,
                    comment = intent.remedy.comment,
                    type = intent.remedy.type,
                    icon = intent.remedy.icon,
                    color = intent.remedy.color,
                    dose = intent.remedy.dose,
                    measureUnit = intent.remedy.measureUnit,
                    photo = intent.remedy.photo,
                    beforeFood = intent.remedy.beforeFood,
                    notUsed = intent.remedy.notUsed,
                )
                createRemedyUseCase(remedy).getOrNull()
            } ?: intent.remedy.id
        // если таблетка старая, то idn не поменялся иначе будет новый
        val remedyIdn = if (remedyId == intent.remedy.id) intent.remedy.idn else 0
        val course = CourseDomainModel(
            id = 0,
            remedyId = remedyId,
            remedyIdn = remedyIdn,
            createdDate = dateNew,
            userId = intent.course.userId,
            userIdn = intent.course.userIdn,
            comment = intent.course.comment,
            startDate = intent.course.startDate,
            endDate = intent.course.endDate,
            remindDate = intent.course.remindDate,
            interval = intent.course.interval,
            regime = intent.course.regime,
            isFinished = false,
            isInfinite = false,
            notUsed = intent.course.notUsed,
        )
        // закроем курс
        closeCourseUseCase(courseId = intent.course.id, dateTime = dateNew)
        // создадим новый курс
        createCourseUseCase(course).onSuccess { courseId ->
            updateUsagesInCourseUseCase(
                courseId = courseId,
                usages = generateUsages(
                    usagesByDay = intent.usagesPattern,
                    remedyId = remedyId,
                    courseId = courseId,
                    userId = course.userId,
                    startDate = course.startDate,
                    endDate = course.endDate,
                    regime = course.regime
                )
            )
        }
    }

    private suspend fun updateCourse(intent: MyCoursesIntent.Update) {
        updateRemedyUseCase(intent.remedy)
        updateCourseUseCase(intent.course)
        updateUsagesInCourseUseCase(
            courseId = intent.course.id,
            usages = generateUsages(
                usagesByDay = intent.usagesPattern,
                remedyId = intent.remedy.id,
                courseId = intent.course.id,
                userId = intent.course.userId,
                startDate = intent.course.startDate,
                endDate = intent.course.endDate,
                regime = intent.course.regime
            )
        )
    }
}
