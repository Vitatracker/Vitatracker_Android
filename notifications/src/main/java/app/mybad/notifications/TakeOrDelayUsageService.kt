package app.mybad.notifications

import android.app.Service
import android.content.Intent
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.scheduler.NotificationsScheduler
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import app.mybad.notifications.AlarmService.Companion.DELAY_INTENT
import app.mybad.notifications.AlarmService.Companion.TAKE_INTENT
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.Extras.REMEDY_ID
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.Extras.USAGE_TIME
import app.mybad.theme.utils.currentDateTime
import app.mybad.theme.utils.toEpochSecond
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TakeOrDelayUsageService : Service() {

    @Inject
    lateinit var usageRepository: UsageRepository

    @Inject
    lateinit var updateUsageUseCase: UpdateUsageUseCase

    @Inject
    lateinit var notificationsScheduler: NotificationsScheduler

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onBind(p0: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            TAKE_INTENT -> {
                scope.launch {
                    usageRepository.getUsagesBetweenByCourseId(
                        intent.getLongExtra(REMEDY_ID.name, 0),
                        intent.getLongExtra(USAGE_TIME.name, 0),
                        intent.getLongExtra(USAGE_TIME.name, 0),
                    ).getOrNull()?.firstOrNull()?.let { usage ->
                        updateUsageUseCase(
                            usage.copy(factUseTime = currentDateTime().toEpochSecond())
                        )
                    }
                }
            }

            DELAY_INTENT -> {
                scope.launch {
                    usageRepository.getUsagesBetweenByCourseId(
                        intent.getLongExtra(REMEDY_ID.name, 0L),
                        intent.getLongExtra(USAGE_TIME.name, 0L),
                        intent.getLongExtra(USAGE_TIME.name, 0L),
                    ).getOrNull()?.firstOrNull()?.let { usage ->
                        notificationsScheduler.addAlarm(
                            listOf(
                                usage.copy(
                                    useTime = currentDateTime().toEpochSecond()
                                        .plus(20) //TODO("что это за 20")
                                )
                            )
                        )
                    }
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
