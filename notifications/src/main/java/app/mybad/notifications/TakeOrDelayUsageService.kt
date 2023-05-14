package app.mybad.notifications

import android.app.Service
import android.content.Intent
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import app.mybad.network.repos.repo.CoursesNetworkRepo
import app.mybad.notifications.AlarmService.Companion.DELAY_INTENT
import app.mybad.notifications.AlarmService.Companion.TAKE_INTENT
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.Extras.MED_ID
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.Extras.USAGE_TIME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@AndroidEntryPoint
class TakeOrDelayUsageService : Service() {

    @Inject lateinit var usagesRepo: UsagesRepo

    @Inject lateinit var coursesNetworkRepo : CoursesNetworkRepo

    @Inject lateinit var updateUseCase: UpdateUsageUseCase

    @Inject lateinit var notificationsScheduler: NotificationsSchedulerImpl
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onBind(p0: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            TAKE_INTENT -> {
                scope.launch {
                    val uDef = usagesRepo.getUsagesByIntervalByMed(
                        intent.getLongExtra(MED_ID.name, 0L),
                        intent.getLongExtra(USAGE_TIME.name, 0L),
                        intent.getLongExtra(USAGE_TIME.name, 0L),
                    ).first()
                    val u = uDef.copy(factUseTime = Instant.now().epochSecond)
                    updateUseCase.execute(u)
                    coursesNetworkRepo.updateUsage(u)
                }
            }
            DELAY_INTENT -> {
                scope.launch {
                    val uDef = usagesRepo.getUsagesByIntervalByMed(
                        intent.getLongExtra(MED_ID.name, 0L),
                        intent.getLongExtra(USAGE_TIME.name, 0L),
                        intent.getLongExtra(USAGE_TIME.name, 0L),
                    ).first()
                    val u = uDef.copy(useTime = Instant.now().epochSecond + 20)
                    notificationsScheduler.add(listOf(u))
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
