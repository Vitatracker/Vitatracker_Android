package app.mybad.domain.usecases.courses

import android.util.Log
import app.mybad.domain.usecases.user.RefreshAuthTokenUseCase
import javax.inject.Inject

class SynchronizationCourseUseCase @Inject constructor(
    private val refreshAuthTokenUseCase: RefreshAuthTokenUseCase,
    private val sendCoursesDeletedToNetworkUseCase: SendCoursesDeletedToNetworkUseCase,
    private val sendCoursesToNetworkUseCase: SendCoursesToNetworkUseCase,
    private val syncCoursesWithNetworkUseCase: SyncCoursesWithNetworkUseCase,
    private val checkCoursesLocalUseCase: CheckCoursesLocalUseCase,

    ) {
    suspend operator fun invoke(currentDate: Long, info: suspend (String) -> Unit = {}) = runCatching {
        Log.d("VTTAG", "SynchronizationCourseUseCase:: Start")
        // проверим токен на окончание и если нужно обновить
        if (refreshAuthTokenUseCase(currentDate)) {
            // удалим, что удалено в локальной базе
            info(REMEDY_CHANNEL_INFO_DELETED)
            sendCoursesDeletedToNetworkUseCase()
            // отправим новое на бек
            info(REMEDY_CHANNEL_INFO_COURSES)
            sendCoursesToNetworkUseCase()
            // синхронизируем локальную базу и бек
            info(REMEDY_CHANNEL_INFO_SYNC)
            syncCoursesWithNetworkUseCase()
            // проверить целостность базы локальной
            info(REMEDY_CHANNEL_INFO_CHECK)
            checkCoursesLocalUseCase()
            info(REMEDY_CHANNEL_INFO_END)
            Log.d("VTTAG", "SynchronizationCourseUseCase:: End")
        }
    }
}

private const val REMEDY_CHANNEL_INFO_COURSES = "courses"
private const val REMEDY_CHANNEL_INFO_SYNC = "synchronization"
private const val REMEDY_CHANNEL_INFO_CHECK = "check"
private const val REMEDY_CHANNEL_INFO_DELETED = "deleted"
private const val REMEDY_CHANNEL_INFO_END = "end"
