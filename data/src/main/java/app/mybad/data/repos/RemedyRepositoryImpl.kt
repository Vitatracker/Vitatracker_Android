package app.mybad.data.repos

import app.mybad.data.db.dao.RemedyDao
import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.repository.RemedyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class RemedyRepositoryImpl @Inject constructor(
    private val db: RemedyDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : RemedyRepository {

    override suspend fun getRemediesByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getRemediesByUserId(userId).mapToDomain()
        }
    }

    override fun getRemedies(userId: Long) = db.getRemedies(userId)
        .map { it.mapToDomain() }
        .flowOn(dispatcher)

    override suspend fun getRemedyById(remedyId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getRemedyById(remedyId).mapToDomain()
        }
    }

    override suspend fun insertRemedy(remedy: RemedyDomainModel) = withContext(dispatcher) {
        Result.runCatching {
            db.addRemedy(remedy.mapToData())
        }
    }

    override suspend fun updateRemedy(remedy: RemedyDomainModel): Unit = withContext(dispatcher) {
        try {
            db.addRemedy(remedy.mapToData())
        } catch (ignore: Throwable) {
        }
    }

    override suspend fun deleteRemedyById(remedyId: Long) = withContext(dispatcher) {
        try {
            db.deleteRemedyById(remedyId)
        } catch (ignore: Throwable) {
        }
    }

    override suspend fun getRemediesByIds(remedyIdList: List<Long>) = withContext(dispatcher) {
        Result.runCatching {
            db.getRemediesByIds(remedyIdList).mapToDomain()
        }
    }

    override suspend fun getRemedyNotUpdateByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getRemedyNotUpdateByUserId(userId).mapToDomain()
        }
    }
}
