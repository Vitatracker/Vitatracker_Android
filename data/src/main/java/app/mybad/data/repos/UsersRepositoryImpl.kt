package app.mybad.data.repos

import app.mybad.data.db.dao.UsersDao
import app.mybad.data.db.entity.UserLocalDataModel
import app.mybad.data.mapToDomain
import app.mybad.domain.models.user.UserLocalDomainModel
import app.mybad.domain.repos.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UsersRepositoryImpl @Inject constructor(
    private val db: UsersDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : UsersRepository {
    override suspend fun insertUser(name: String, email: String): Long? =
        withContext(dispatcher) {
            db.insert(UserLocalDataModel(name = name, email = email))
        }

    override suspend fun getUserId(email: String): Long? = withContext(dispatcher) {
        db.getUserId(email)
    }

    override suspend fun getUser(userId: Long): UserLocalDomainModel = withContext(dispatcher) {
        db.getUser(userId).mapToDomain()
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
        }
    }

    override suspend fun deleteUser(userId: Long) {
        withContext(dispatcher) {
            db.deleteFromUserId(userId)
        }
    }

}
