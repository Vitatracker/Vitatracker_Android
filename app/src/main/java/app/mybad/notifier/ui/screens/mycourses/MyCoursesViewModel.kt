package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import app.mybad.data.models.EditCourseState
import app.mybad.data.repos.SynchronizationCourseWorker.Companion.start
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.usecases.courses.CloseCourseUseCase
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.courses.DeleteCourseFullUseCase
import app.mybad.domain.usecases.courses.GetCourseByIdUseCase
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.domain.usecases.courses.SynchronizationCourseUseCase
import app.mybad.domain.usecases.courses.UpdateCourseUseCase
import app.mybad.domain.usecases.remedies.CreateRemedyUseCase
import app.mybad.domain.usecases.remedies.GetRemedyByIdUseCase
import app.mybad.domain.usecases.remedies.UpdateRemedyUseCase
import app.mybad.domain.usecases.usages.GetUsagesByCourseIdUseCase
import app.mybad.domain.usecases.usages.GetUseUsagesInCourseUseCase
import app.mybad.domain.usecases.usages.UpdateUsagesInCourseUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.ui.common.generatePattern
import app.mybad.notifier.ui.common.generateUsages
import app.mybad.utils.currentDateTimeInSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    private val loadCoursesUseCase: LoadCoursesUseCase,

    private val getRemedyByIdUseCase: GetRemedyByIdUseCase,
    private val createRemedyUseCase: CreateRemedyUseCase,
    private val updateRemedyUseCase: UpdateRemedyUseCase,

    private val getCourseByIdUseCase: GetCourseByIdUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase,
    private val closeCourseUseCase: CloseCourseUseCase,

    private val getUsagesByCourseIdUseCase: GetUsagesByCourseIdUseCase,
    private val getUseUsagesInCourseUseCase: GetUseUsagesInCourseUseCase,
    private val updateUsagesInCourseUseCase: UpdateUsagesInCourseUseCase,

    private val deleteCourseFullUseCase: DeleteCourseFullUseCase,
    private val synchronizationCourseUseCase: SynchronizationCourseUseCase,
    private val workSync: WorkManager,
) : BaseViewModel<MyCoursesContract.Event, MyCoursesContract.State, MyCoursesContract.Effect>() {

    init {
        Log.w("VTTAG", "MyCoursesViewModel: init")
    }

    private var isCourseUploaded = false

    override fun setInitialState() = MyCoursesContract.State()

    override fun handleEvents(event: MyCoursesContract.Event) {
//        TODO("Not yet implemented")
        viewModelScope.launch {
            when (event) {
                is MyCoursesContract.Event.Delete -> {
                    //TODO("проверить было ли использование")
                    deleteCourseFullUseCase(event.courseId, currentDateTimeInSecond())
                    setEffect { MyCoursesContract.Effect.Navigation.Back }
                    //TODO("запустить воркер удаления курса на беке")
                    // синхронизировать
                    syncCourseToServer()
                }

                is MyCoursesContract.Event.Update -> {
                    //TODO("тут необходимо переделать, закрывать старый курс и пересоздавать курс с новой датой")
                    // проверить есть ли использование таблетки
                    val isUseUsage = getUseUsagesInCourseUseCase(event.course.id)
                        .getOrNull() ?: false
                    if (isUseUsage) {
                        // таблетки уже были выпиты, закрыть с текущай датой курс и создать новый
                        createNewCourse(event)
                    } else {
                        // курс не использовался, можно изменять без последствий
                        updateCourse(event)
                    }
                    setEffect { MyCoursesContract.Effect.Navigation.Back }
                    // синхронизировать
                    syncCourseToServer()
                }

                is MyCoursesContract.Event.CourseEditing -> {
                    setEffect { MyCoursesContract.Effect.Navigation.ToCourseEditing(event.courseId) }
                }

                MyCoursesContract.Event.ActionBack -> setEffect { MyCoursesContract.Effect.Navigation.Back }
            }
        }
    }

    private suspend fun createNewCourse(event: MyCoursesContract.Event.Update) {
        val dateNew = currentDateTimeInSecond()
        // сравнить таблетку и создать новую если нужно
        val remedyId = getRemedyByIdUseCase(event.remedy.id).getOrNull()
            ?.takeIf { it != event.remedy }?.let {
                val remedy = RemedyDomainModel(
                    id = 0,
                    createdDate = dateNew,
                    userId = event.remedy.userId,
                    userIdn = event.remedy.userIdn,
                    name = event.remedy.name,
                    description = event.remedy.description,
                    comment = event.remedy.comment,
                    type = event.remedy.type,
                    icon = event.remedy.icon,
                    color = event.remedy.color,
                    dose = event.remedy.dose,
                    measureUnit = event.remedy.measureUnit,
                    photo = event.remedy.photo,
                    beforeFood = event.remedy.beforeFood,
                    notUsed = event.remedy.notUsed,
                )
                createRemedyUseCase(remedy).getOrNull()
            } ?: event.remedy.id
        // если таблетка старая, то idn не поменялся иначе будет новый
        val remedyIdn = if (remedyId == event.remedy.id) event.remedy.idn else 0
        val course = CourseDomainModel(
            id = 0,
            remedyId = remedyId,
            remedyIdn = remedyIdn,
            createdDate = dateNew,
            userId = event.course.userId,
            userIdn = event.course.userIdn,
            comment = event.course.comment,
            startDate = event.course.startDate,
            endDate = event.course.endDate,
            remindDate = event.course.remindDate,
            interval = event.course.interval,
            regime = event.course.regime,
            isFinished = false,
            isInfinite = false,
            notUsed = event.course.notUsed,
        )
        // закроем курс
        closeCourseUseCase(courseId = event.course.id, dateTime = dateNew)
        // создадим новый курс
        createCourseUseCase(course).onSuccess { courseId ->
            updateUsagesInCourseUseCase(
                courseId = courseId,
                usages = generateUsages(
                    usagesByDay = event.usagesPattern,
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

    private suspend fun updateCourse(event: MyCoursesContract.Event.Update) {
        updateRemedyUseCase(event.remedy)
        updateCourseUseCase(event.course)
        updateUsagesInCourseUseCase(
            courseId = event.course.id,
            usages = generateUsages(
                usagesByDay = event.usagesPattern,
                remedyId = event.remedy.id,
                courseId = event.course.id,
                userId = event.course.userId,
                startDate = event.course.startDate,
                endDate = event.course.endDate,
                regime = event.course.regime
            )
        )
    }

    private suspend fun syncCourseToServer() {
        synchronizationCourseUseCase(currentDateTimeInSecond()).onFailure {
            // если ошибка то запустим воркер
            workSync.start()
        }
    }

    fun uploadCoursesInState() {
        if (isCourseUploaded) return
        isCourseUploaded = true
        viewModelScope.launch {
            loadCoursesUseCase().collectLatest { (courses, remedies, usages) ->
                Log.w(
                    "VTTAG",
                    "MyCoursesViewModel::loadCoursesInState: remedies=${remedies.size} courses=${
                        courses.size
                    } usages=${usages.size}"
                )
                setState {
                    copy(
                        courses = courses,
                        remedies = remedies,
                        usages = usages,
                    )
                }
            }
        }
    }

    fun uploadCourseForEditingInState(courseId: Long) {
        Log.w("VTTAG", "MyCoursesViewModel::loadEditCourseInState: courseId=$courseId")
        if (isCourseUploaded) return
        isCourseUploaded = true
        viewModelScope.launch {
            getCourseByIdUseCase(courseId).getOrNull()?.let { course ->
                getRemedyByIdUseCase(course.remedyId).getOrNull()?.let { remedy ->
                    val usages = getUsagesByCourseIdUseCase(courseId).getOrNull() ?: emptyList()
                    val usagesPattern = generatePattern(course.id, course.regime, usages)
                    setState {
                        copy(
                            editCourse = EditCourseState(
                                course = course,
                                remedy = remedy,
                                usagesPattern = usagesPattern
                            ),
                        )
                    }
                }
            }
            Log.w(
                "VTTAG", "MyCoursesViewModel::loadEditCourseInState: courseId=${
                    courseId
                } usages=${viewState.value.editCourse.usagesPattern.size} usages=${viewState.value.editCourse.usagesPattern}"
            )
        }
    }

}
