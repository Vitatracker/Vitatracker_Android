package app.mybad.data.repos

import android.util.Log
import app.mybad.data.db.dao.UserDao
import app.mybad.data.db.models.UserModel
import app.mybad.data.mapToDomain
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserPersonalDomainModel
import app.mybad.domain.repository.UserRepository
import app.mybad.utils.currentDateTimeInSeconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    private val db: UserDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : UserRepository {

    override fun isDarkTheme(userId: Long) = db.isDarkTheme(userId)
        .catch {
            Log.w("VTTAG", "UserRepositoryImpl::isDarkTheme: error userId=$userId", it)
        }
        .flowOn(dispatcher)

    override suspend fun getNumberOfUsers() = withContext(dispatcher) {
        try {
            db.getNumberOfUsers()
        } catch (ignore: Error) {
            0
        }
    }

    override suspend fun getUserIdByEmail(email: String) = withContext(dispatcher) {
        try {
            db.getUserByEmail(email)?.id
        } catch (ignore: Error) {
            null
        }
    }

    override suspend fun getUserByEmail(email: String): UserDomainModel = withContext(dispatcher) {
        db.getUserByEmail(email)?.mapToDomain() ?: UserDomainModel()
    }

    override suspend fun getUserById(userId: Long): UserDomainModel = withContext(dispatcher) {
        db.getUser(userId).mapToDomain()
    }

    override suspend fun getUserPersonal(userId: Long): UserPersonalDomainModel =
        withContext(dispatcher) {
            db.getUserPersonal(userId)?.mapToDomain() ?: UserPersonalDomainModel()
        }

    override suspend fun getUserLastEntrance(): UserDomainModel = withContext(dispatcher) {
        db.getUserLastEntrance()?.mapToDomain() ?: UserDomainModel()
    }

    override suspend fun insertUser(name: String, email: String) = withContext(dispatcher) {
        db.insert(UserModel(name = name, email = email, createdDate = currentDateTimeInSeconds()))
    }

    override suspend fun updateMail(userId: Long, email: String) {
        withContext(dispatcher) {
            val user = db.getUser(userId)
            db.updateUser(user.copy(email = email))
        }
    }

    override suspend fun updateName(userId: Long, name: String) {
        withContext(dispatcher) {
            val user = db.getUser(userId)
            db.updateUser(user.copy(name = name))
            Log.w(
                "VTTAG",
                "UserRepositoryImpl::updateName: Ok: userId=${user.id} name=$name"
            )
        }
    }

    override suspend fun updateNotificationDate(userId: Long) {
        withContext(dispatcher) {
            try {
                db.updateNotificationDate(userId = userId)
            } catch (_: Error) {
            }
        }
    }

    override suspend fun clearTokenByUserId(userId: Long) {
        withContext(dispatcher) {
            val user = db.getUser(userId)
            db.updateUser(
                user.copy(
                    token = "",
                    tokenDate = 0,
                    tokenRefresh = "",
                    tokenRefreshDate = 0,

                    updatedDate = currentDateTimeInSeconds(),
                )
            )
        }
    }

    override suspend fun updateTokenByUserId(
        userId: Long,
        token: String,
        tokenDate: Long,
        tokenRefresh: String,
        tokenRefreshDate: Long,
    ): UserDomainModel = withContext(dispatcher) {
        val user = db.getUser(userId).copy(
            token = token,
            tokenDate = tokenDate,
            tokenRefresh = tokenRefresh,
            tokenRefreshDate = tokenRefreshDate,

            updatedDate = currentDateTimeInSeconds(),
        )
        Log.w(
            "VTTAG",
            "UserRepositoryImpl::updateTokenByUserId: Ok: userId=${user.id} token=${user.token}"
        )
        db.updateUser(user)
        user.mapToDomain()
    }

    override suspend fun markDeletionUserById(userId: Long) {
        withContext(dispatcher) {
            db.markDeletionUserById(userId)
        }
    }

    override suspend fun deleteUserById(userId: Long) {
        withContext(dispatcher) {
            db.deleteUserById(userId)
        }
    }

    override suspend fun updateDateSynchronize(userId: Long) {
        withContext(dispatcher) {
            runCatching {
                db.synchronization(userId)
            }
        }
    }

}
