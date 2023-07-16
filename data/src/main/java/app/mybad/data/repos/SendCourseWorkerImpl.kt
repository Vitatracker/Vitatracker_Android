package app.mybad.data.repos

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.mybad.data.db.dao.CourseDao
import app.mybad.data.db.dao.RemedyDao
import app.mybad.data.db.dao.UsageDao
import app.mybad.domain.repository.network.CourseNetworkRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Named

@HiltWorker
class SendCourseWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val remedyDao: RemedyDao,
    private val courseDao: CourseDao,
    private val usageDao: UsageDao,
    private val repository: CourseNetworkRepository,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        return withContext(dispatcher) {
            try {
/*
                val remedy = remedyDao.getRemedyNotUpdate(AuthToken.userId)
                repository.updateAll(
                    remedy = remedy.mapToDomain()
                )
*/
                Result.success()
            } catch (t: Throwable) {
                Result.retry()
            }
        }
    }

    companion object {
    }
}
