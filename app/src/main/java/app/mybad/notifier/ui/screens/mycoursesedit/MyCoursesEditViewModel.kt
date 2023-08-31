package app.mybad.notifier.ui.screens.mycoursesedit

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.usecases.courses.CloseCourseUseCase
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.courses.DeleteCourseFullUseCase
import app.mybad.domain.usecases.courses.GetCourseByIdUseCase
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
import app.mybad.utils.SECONDS_IN_DAY
import app.mybad.utils.changeTime
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.hour
import app.mybad.utils.minus
import app.mybad.utils.minute
import app.mybad.utils.plus
import app.mybad.utils.plusDay
import app.mybad.utils.timeInMinutes
import app.mybad.utils.toEpochSecond
import app.mybad.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimePeriod
import javax.inject.Inject

@HiltViewModel
class MyCoursesEditViewModel @Inject constructor(
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
) : BaseViewModel<MyCoursesEditContract.Event, MyCoursesEditContract.State, MyCoursesEditContract.Effect>() {

    init {
        Log.w("VTTAG", "MyCoursesViewModel: init")
    }

    private var courseId = 0L

    override fun setInitialState() = MyCoursesEditContract.State()

    override fun handleEvents(event: MyCoursesEditContract.Event) {
//        TODO("Not yet implemented")
        viewModelScope.launch {
            when (event) {
                is MyCoursesEditContract.Event.Delete -> {
                    //TODO("проверить было ли использование")
                    deleteCourseFullUseCase(event.courseId, currentDateTimeInSecond())
                    //TODO("запустить воркер удаления курса на беке")
                    // синхронизировать
                    syncCourseToServer()
                    setEffect { MyCoursesEditContract.Effect.Navigation.Back }
                }

                MyCoursesEditContract.Event.AddUsagesPattern -> addUsagesPattern()

                is MyCoursesEditContract.Event.DeleteUsagePattern -> setState {
                    copy(usagesPatternEdit = usagesPatternEdit.minus(event.pattern))
                }

                is MyCoursesEditContract.Event.ChangeQuantityUsagePattern -> {
                    changeQuantityUsagePattern(event.pattern, event.quantity)
                }

                is MyCoursesEditContract.Event.ChangeTimeUsagePattern -> {
                    changeTimeUsagePattern(event.pattern, event.time)
                }

                is MyCoursesEditContract.Event.UpdateAndEnd -> {
                    updateRemedyAndCourse(remedy = event.remedy, course = event.course)
                    saveCourse()
                    // синхронизировать
                    syncCourseToServer()
                    setEffect { MyCoursesEditContract.Effect.Navigation.Back }
                }

                MyCoursesEditContract.Event.NotificationEditing -> setEffect {
                    MyCoursesEditContract.Effect.Navigation.NotificationEditing
                }

                is MyCoursesEditContract.Event.NotificationUpdateAndEnd -> {
                    updateReminder(
                        remindTime = event.remindTime,
                        coursesInterval = event.coursesInterval,
                        remindBeforePeriod = event.remindBeforePeriod
                    )
                    setEffect { MyCoursesEditContract.Effect.Navigation.Back }
                }

                MyCoursesEditContract.Event.ActionBack -> {
                    cancellation()
                    setEffect { MyCoursesEditContract.Effect.Navigation.Back }
                }
            }
        }
    }

    fun uploadCourseForEditingInState(courseId: Long) {
        Log.w("VTTAG", "MyCoursesViewModel::loadEditCourseInState: courseId=$courseId")
        if (this.courseId == courseId) return
        this.courseId = courseId
        viewModelScope.launch {
            getCourseByIdUseCase(courseId).getOrNull()?.let { course ->
                getRemedyByIdUseCase(course.remedyId).getOrNull()?.let { remedy ->
                    val usages = getUsagesByCourseIdUseCase(courseId).getOrNull() ?: emptyList()
                    val usagesPattern = generatePattern(course.id, course.regime, usages)

                    // начало нового курса + интервал за сколько дней сообщить - последний день курса, который может быть больше
                    val beforeDay = if (course.remindDate > 0) {
                        course.remindDate.plusDay(course.interval.toInt())
                            .minus(course.endDate) / SECONDS_IN_DAY
                    } else 0

                    setState {
                        copy(
                            course = course,
                            remedy = remedy,
                            usagesPattern = usagesPattern,

                            usagesPatternEdit = usagesPattern,
                            remindTime = viewState.value.course.remindDate.timeInMinutes(),
                            coursesInterval = DateTimePeriod(days = course.interval.toInt()),
                            remindBeforePeriod = DateTimePeriod(days = beforeDay.toInt()),
                        )
                    }
                }
            }

            Log.w(
                "VTTAG", "MyCoursesViewModel::loadEditCourseInState: courseId=${
                    courseId
                } usages=${viewState.value.usagesPatternEdit.size} usages=${viewState.value.usagesPatternEdit}"
            )
        }
    }

    private fun updateStateUsagesPatterns(usagesPattern: List<UsageFormat>) {
        setState { copy(usagesPatternEdit = usagesPattern) }
    }

    private fun changeTimeUsagePattern(pattern: UsageFormat, time: Int) {
        updateStateUsagesPatterns(
            UsageFormat.changeTimeUsagePattern(
                usagesPattern = viewState.value.usagesPatternEdit,
                pattern = pattern,
                time = time
            )
        )
    }

    private fun changeQuantityUsagePattern(pattern: UsageFormat, quantity: Int) {
        updateStateUsagesPatterns(
            UsageFormat.changeQuantityUsagePattern(
                usagesPattern = viewState.value.usagesPatternEdit,
                pattern = pattern,
                quantity = quantity
            )
        )
    }

    private fun addUsagesPattern() {
        updateStateUsagesPatterns(
            UsageFormat.addUsagesPattern(
                usagesPattern = viewState.value.usagesPatternEdit
            )
        )
    }

    private fun updateRemedyAndCourse(
        remedy: RemedyDomainModel,
        course: CourseDomainModel,
    ) {
        val remindDate = course.endDate.toLocalDateTime()
            .plus(viewState.value.coursesInterval)
            .minus(viewState.value.remindBeforePeriod)
            .changeTime(
                hour = viewState.value.remindTime.hour(),
                minute = viewState.value.remindTime.minute()
            )
            .toEpochSecond()
        setState {
            copy(
                remedy = remedy,
                course = course.copy(
                    remindDate = remindDate,
                    interval = viewState.value.remindBeforePeriod.days.toLong()
                ),
            )
        }
    }

    private fun updateReminder(
        remindTime: Int,
        coursesInterval: DateTimePeriod,
        remindBeforePeriod: DateTimePeriod,
    ) {
        val remindDate = viewState.value.course.endDate.toLocalDateTime()
            .plus(coursesInterval)
            .minus(remindBeforePeriod)
            .changeTime(
                hour = remindTime.hour(),
                minute = remindTime.minute()
            )
            .toEpochSecond()
        setState {
            copy(
                remindTime = remindTime,
                coursesInterval = coursesInterval,
                remindBeforePeriod = remindBeforePeriod,
                usagesPattern = viewState.value.usagesPatternEdit,

                course = viewState.value.course.copy(
                    remindDate = remindDate,
                    interval = remindBeforePeriod.days.toLong()
                ),
            )
        }
    }

    private fun cancellation() {
        setState {
            copy(
                usagesPatternEdit = viewState.value.usagesPattern
            )
        }
    }

    private suspend fun saveCourse() {
        //TODO("тут необходимо переделать, закрывать старый курс и пересоздавать курс с новой датой")
        // проверить есть ли использование таблетки
        val isUseUsage = getUseUsagesInCourseUseCase(viewState.value.course.id)
            .getOrNull() ?: false
        if (isUseUsage) {
            // таблетки уже были выпиты, закрыть с текущай датой курс и создать новый
            createNewCourse()
        } else {
            // курс не использовался, можно изменять без последствий
            updateCourse()
        }
    }

    private suspend fun createNewCourse() {
        val dateNew = currentDateTimeInSecond()
        // сравнить таблетку и создать новую если нужно
        val remedyId = getRemedyByIdUseCase(viewState.value.remedy.id).getOrNull()
            ?.takeIf { it != viewState.value.remedy }?.let {
                val remedy = RemedyDomainModel(
                    id = 0,
                    createdDate = dateNew,
                    userId = viewState.value.remedy.userId,
                    userIdn = viewState.value.remedy.userIdn,
                    name = viewState.value.remedy.name,
                    description = viewState.value.remedy.description,
                    comment = viewState.value.remedy.comment,
                    type = viewState.value.remedy.type,
                    icon = viewState.value.remedy.icon,
                    color = viewState.value.remedy.color,
                    dose = viewState.value.remedy.dose,
                    measureUnit = viewState.value.remedy.measureUnit,
                    photo = viewState.value.remedy.photo,
                    beforeFood = viewState.value.remedy.beforeFood,
                    notUsed = viewState.value.remedy.notUsed,
                )
                createRemedyUseCase(remedy).getOrNull()
            } ?: viewState.value.remedy.id
        // если таблетка старая, то idn не поменялся иначе будет новый
        val remedyIdn = if (remedyId == viewState.value.remedy.id) viewState.value.remedy.idn else 0
        val course = CourseDomainModel(
            id = 0,
            remedyId = remedyId,
            remedyIdn = remedyIdn,
            createdDate = dateNew,
            userId = viewState.value.course.userId,
            userIdn = viewState.value.course.userIdn,
            comment = viewState.value.course.comment,
            startDate = viewState.value.course.startDate,
            endDate = viewState.value.course.endDate,
            remindDate = viewState.value.course.remindDate,
            interval = viewState.value.course.interval,
            regime = viewState.value.course.regime,
            isFinished = false,
            isInfinite = false,
            notUsed = viewState.value.course.notUsed,
        )
        // закроем курс
        closeCourseUseCase(courseId = viewState.value.course.id, dateTime = dateNew)
        // создадим новый курс
        createCourseUseCase(course).onSuccess { courseId ->
            updateUsagesInCourseUseCase(
                courseId = courseId,
                usages = generateUsages(
                    usagesByDay = viewState.value.usagesPattern,
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

    private suspend fun updateCourse() {
        updateRemedyUseCase(viewState.value.remedy)
        updateCourseUseCase(viewState.value.course)
        updateUsagesInCourseUseCase(
            courseId = viewState.value.course.id,
            usages = generateUsages(
                usagesByDay = viewState.value.usagesPattern,
                remedyId = viewState.value.remedy.id,
                courseId = viewState.value.course.id,
                userId = viewState.value.course.userId,
                startDate = viewState.value.course.startDate,
                endDate = viewState.value.course.endDate,
                regime = viewState.value.course.regime
            )
        )
    }

    private suspend fun syncCourseToServer() {
        viewModelScope.launch {
            synchronizationCourseUseCase(currentDateTimeInSecond()).onSuccess {

            }.onFailure {

            }
        }
    }

}
