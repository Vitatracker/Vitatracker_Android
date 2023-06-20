package app.mybad.domain.repos

import app.mybad.domain.models.user.UserLocalDomainModel

interface UsersRepository {

    suspend fun insertUser(name: String, email: String): Long?
    suspend fun getUserId(email: String): Long?
    suspend fun getUser(userId: Long): UserLocalDomainModel
    suspend fun updateMail(userId: Long, email: String)
    suspend fun updateName(userId: Long, name: String)
    suspend fun deleteUser(userId: Long)
}
