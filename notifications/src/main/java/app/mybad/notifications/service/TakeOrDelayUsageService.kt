package app.mybad.notifications.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import app.mybad.domain.scheduler.NotificationsScheduler
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.DELAY_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.TAKE_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.USAGE_NOTIFICATION_ID
import app.mybad.notifications.Extras
import app.mybad.utils.DELAY_INTENT_SECONDS
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.displayDateTime
import app.mybad.utils.plusSeconds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TakeOrDelayUsageService : Service() {

    @Inject
    lateinit var notificationsScheduler: NotificationsScheduler

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(p0: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let(::process)
        return START_NOT_STICKY
    }

    private fun process(intent: Intent) {
        when (intent.action) {
            TAKE_INTENT -> {
                scope.launch {
                    val patternId = intent.getLongExtra(Extras.USAGE_ID.name, 0)
                    val useTime = intent.getLongExtra(Extras.USAGE_TIME.name, 0)
                    log("TAKE_INTENT: patternId=$patternId useTime=${useTime.displayDateTime()}")
                    clearNotification(patternId, intent.data)
                    if (patternId > 0) notificationsScheduler.setFactUseTime(
                        patternId = patternId,
                        useTime = useTime
                    )
                }
            }

            DELAY_INTENT -> {
                scope.launch {
                    val patternId = intent.getLongExtra(Extras.USAGE_ID.name, 0)
                    log("DELAY_INTENT: patternId=$patternId")
                    clearNotification(patternId, intent.data)
                    if (patternId > 0) notificationsScheduler.addAlarmByPatternUsageId(
                        patternId = patternId,
                        notificationTime = currentDateTimeSystem()
                            .plusSeconds(DELAY_INTENT_SECONDS) //TODO("что это? и зачем")
                    )
                }
            }
        }
    }

    private fun clearNotification(patternId: Long, data: Uri?) {
        NotificationManagerCompat.from(baseContext).cancel(
            if (patternId > 0) USAGE_NOTIFICATION_ID + patternId.toInt()
            else data?.authority?.toIntOrNull() ?: 0
        )
    }

    private fun log(text: String, error: Error? = null) {
        if (error == null) {
            Log.w("VTTAG", "NotifiTag::TakeOrDelayUsageService::$text")
        } else {
            Log.e("VTTAG", "NotifiTag::TakeOrDelayUsageService::$text", error)
        }
    }

    override fun onDestroy() {
        if (scope.isActive) scope.cancel()
        super.onDestroy()
    }
}
