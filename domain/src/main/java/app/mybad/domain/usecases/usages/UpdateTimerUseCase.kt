package app.mybad.domain.usecases.usages

import android.os.CountDownTimer
import android.util.Log
import app.mybad.utils.currentDateTimeInSeconds
import app.mybad.utils.displayTime
import javax.inject.Inject

class UpdateTimerUseCase @Inject constructor() {

    private var countDownTimer: CountDownTimer? = null
    private var endTime: Long = 0

    fun start(timer: Long, onTimerTick: (Long) -> Unit = {}, onFinish: () -> Unit = {}) {
        Log.w("VTTAG", "UpdateTimerUseCase::startUpdateTimer: in")
        if (timer == endTime) return
        Log.w(
            "VTTAG",
            "UpdateTimerUseCase::startUpdateTimer: start=${
                currentDateTimeInSeconds().displayTime()
            } end=${timer.displayTime()}"
        )
        endTime = timer
        initTimer(onTimerTick, onFinish)
    }

    fun stop() {
        Log.w("VTTAG", "UpdateTimerUseCase::startUpdateTimer: stop")
        countDownTimer?.cancel()
        countDownTimer = null
    }

    private fun onTimerTick(onTimerTick: (Long) -> Unit, onFinish: () -> Unit) {
        val seconds = endTime - currentDateTimeInSeconds()
        if (seconds > 0) onTimerTick(seconds) else onFinish()
    }

    private fun initTimer(onTimerTick: (Long) -> Unit, onFinish: () -> Unit) {
        stop()
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTimerTick(onTimerTick, onFinish)
            }

            override fun onFinish() {
                onFinish()
            }
        }
        countDownTimer?.start()
    }
}
