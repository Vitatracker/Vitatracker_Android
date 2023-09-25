package app.mybad.notifier.ui.screens.mycoursesedit

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.usecases.courses.CloseCourseUseCase
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.courses.DeleteCourseFullUseCase
import app.mybad.domain.usecases.courses.GetCourseByIdUseCase
import app.mybad.domain.usecases.courses.UpdateCourseUseCase
import app.mybad.domain.usecases.remedies.CreateRemedyUseCase
import app.mybad.domain.usecases.remedies.GetRemedyByIdUseCase
import app.mybad.domain.usecases.remedies.UpdateRemedyUseCase
import app.mybad.domain.usecases.usages.GetPatternUsagesByCourseIdUseCase
import app.mybad.domain.usecases.usages.GetUseUsagesInCourseUseCase
import app.mybad.domain.usecases.usages.UpdatePatternUsagesByCourseIdUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.currentDateTimeUTCInSecond
import app.mybad.utils.displayDateTime
import app.mybad.utils.nextCourseIntervals
import app.mybad.utils.nextCourseStart
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

    private val getPatternUsagesByCourseIdUseCase: GetPatternUsagesByCourseIdUseCase,
    private val updatePatternUsagesByCourseIdUseCase: UpdatePatternUsagesByCourseIdUseCase,

    private val getUseUsagesInCourseUseCase: GetUseUsagesInCourseUseCase,

    private val deleteCourseFullUseCase: DeleteCourseFullUseCase,
) : BaseViewModel<MyCoursesEditContract.Event, MyCoursesEditContract.State, MyCoursesEditContract.Effect>() {

    init {
        Log.w("VTTAG", "MyCoursesViewModel: init")
    }

    private var courseIdLocked = 0L

    override fun setInitialState() = MyCoursesEditContract.State()

    override fun handleEvents(event: MyCoursesEditContract.Event) {
//        TODO("Not yet implemented")
        viewModelScope.launch {
            when (event) {
                is MyCoursesEditContract.Event.Delete -> {
                    //TODO("проверить было ли использование")
                    deleteCourseFullUseCase(event.courseId, currentDateTimeSystem())
                    //TODO("запустить воркер удаления курса на беке")
                    // синхронизировать
                    AuthToken.requiredSynchronize(currentDateTimeUTCInSecond())
                    setEffect { MyCoursesEditContract.Effect.Navigation.Back }
                }

                is MyCoursesEditContract.Event.DeleteUsagePattern -> {
                    updateStateUsagesPatterns(viewState.value.usagesPatternEdit.minus(event.pattern))
                }

                MyCoursesEditContract.Event.AddUsagesPattern -> addUsagesPattern()

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
                    AuthToken.requiredSynchronize(currentDateTimeUTCInSecond())
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
        if (this.courseIdLocked == courseId) return
        this.courseIdLocked = courseId
        viewModelScope.launch {
            getCourseByIdUseCase(courseId).getOrNull()?.let { course ->
                getRemedyByIdUseCase(course.remedyId).getOrNull()?.let { remedy ->
                    // загрузим паттерн для курса
                    val usagesPattern = getPatternUsagesByCourseIdUseCase(courseId)
                        .getOrNull()?.map { pattern ->
                            UsageFormat(
                                timeInMinutes = pattern.timeInMinutes,
                                quantity = pattern.quantity,
                            )
                        } ?: emptyList()

                    // тут время в локальном часовом поясе
                    val (remindTime, interval, beforeDay) = course.endDate.nextCourseIntervals(
                        remindDate = course.remindDate,
                        interval = course.interval,
                    )
                    setState {
                        copy(
                            course = course,
                            remedy = remedy,
                            usagesPattern = usagesPattern,

                            usagesPatternEdit = usagesPattern,
                            nextAllowed = usagesPattern.isNotEmpty(),
                            remindTime = remindTime,
                            coursesInterval = DateTimePeriod(days = interval),
                            remindBeforePeriod = DateTimePeriod(days = beforeDay),
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
        setState {
            copy(
                usagesPatternEdit = usagesPattern,
                nextAllowed = usagesPattern.isNotEmpty()
            )
        }
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

    private fun changeQuantityUsagePattern(pattern: UsageFormat, quantity: Float) {
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
        setState {
            copy(
                remedy = remedy,
                course = course,
            )
        }
        updateReminder(
            remindTime = viewState.value.remindTime,
            coursesInterval = viewState.value.coursesInterval,
            remindBeforePeriod = viewState.value.remindBeforePeriod,
        )
    }

    private fun updateReminder(
        remindTime: Int,
        coursesInterval: DateTimePeriod,
        remindBeforePeriod: DateTimePeriod,
    ) {
        val (remindDate, interval) = viewState.value.course.endDate.nextCourseStart(
            remindTime = remindTime,
            coursesInterval = coursesInterval,
            remindBeforePeriod = remindBeforePeriod,
        )
        setState {
            copy(
                remindTime = remindTime,
                coursesInterval = coursesInterval,
                remindBeforePeriod = remindBeforePeriod,

                course = viewState.value.course.copy(
                    remindDate = remindDate,
                    interval = interval,
                ),
            )
        }
        Log.w(
            "VTTAG",
            "MyCoursesViewModel::updateReminder: endDay=${viewState.value.course.endDate.displayDateTime()} remindDate=${remindDate?.displayDateTime()} coursesInterval=${coursesInterval.months}:${coursesInterval.days} interval=${viewState.value.course.interval}"
        )
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
        val dateNew = currentDateTimeUTCInSecond()
        // сравнить таблетку и создать новую если нужно
        val remedyId = getRemedyByIdUseCase(viewState.value.remedy.id).getOrNull()
            ?.takeIf { it != viewState.value.remedy }?.let {
                val remedy = RemedyDomainModel(
                    id = 0,
                    createdDate = 0,
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
        val remedyIdn = if (remedyId == viewState.value.remedy.id) viewState.value.remedy.idn else {
            Log.w("VTTAG", "MyCoursesViewModel::createNewCourse: new remedyId=$remedyId")
            0
        }
        // TODO("не понятно какая тут дата старта курса?")
        val course = viewState.value.course.copy(
            id = 0,

            remedyId = remedyId,
            remedyIdn = remedyIdn,

            isInfinite = false,
            isFinished = false,
            notUsed = false,

            createdDate = 0,// запишется в мапере

            updateNetworkDate = 0,
        )
        Log.w(
            "VTTAG",
            "MyCoursesViewModel::createNewCourse: closed courseId=${viewState.value.course.id}"
        )
        // закроем курс
        closeCourseUseCase(courseId = viewState.value.course.id, endDate = currentDateTimeSystem()) // с учетом часового пояса
        // создадим новый курс
        createCourseUseCase(course).onSuccess { courseId ->
            Log.w("VTTAG", "MyCoursesViewModel::createNewCourse: new courseId=$courseId")
            // обновим паттерн
            updatePatternUsages(
                courseId = courseId,
                usagesPattern = viewState.value.usagesPatternEdit
            )
        }
    }

    private suspend fun updateCourse() {
        Log.w("VTTAG", "MyCoursesViewModel::updateCourse: courseId=${viewState.value.course.id}")
        updateRemedyUseCase(viewState.value.remedy.copy(updateNetworkDate = 0))
        updateCourseUseCase(viewState.value.course.copy(updateNetworkDate = 0))
        // обновим паттерн, только если нужно
        if (viewState.value.usagesPattern != viewState.value.usagesPatternEdit) {
            Log.w(
                "VTTAG",
                "MyCoursesViewModel::updateCourse: updatePatternUsages=${viewState.value.usagesPatternEdit.size}"
            )
            updatePatternUsages(
                courseId = viewState.value.course.id,
                usagesPattern = viewState.value.usagesPatternEdit
            )
        }
    }

    private suspend fun updatePatternUsages(courseId: Long, usagesPattern: List<UsageFormat>) {
        updatePatternUsagesByCourseIdUseCase(
            courseId = courseId,
            patterns = usagesPattern.map { pattern ->
                PatternUsageDomainModel(
                    courseId = courseId,
                    timeInMinutes = pattern.timeInMinutes, // тут время с учетом часового пояса
                    quantity = pattern.quantity,
                )
            }
        )
    }
}
