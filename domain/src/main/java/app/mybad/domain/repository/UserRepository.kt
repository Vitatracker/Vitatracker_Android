package app.mybad.domain.repository

import app.mybad.domain.models.user.UserDomainModel

interface UserRepository {

    suspend fun insertUser(name: String, email: String): Long?
    suspend fun getUserIdByEmail(email: String): Long?
    suspend fun getUserByEmail(email: String): UserDomainModel
    suspend fun getUserById(userId: Long): UserDomainModel
    suspend fun getUserLastEntrance(): UserDomainModel
    suspend fun updateMail(userId: Long, email: String)
    suspend fun updateName(userId: Long, name: String)
    suspend fun deleteUser(userId: Long)
    suspend fun clearTokenByUserId(userId: Long)
    suspend fun updateTokenByUserId(
        userId: Long,
        token: String,
        tokenDate: Long,
        tokenRefresh: String,
        tokenRefreshDate: Long,
    ): UserDomainModel
}
