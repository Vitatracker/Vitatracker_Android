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

    override fun getRemedies(userId: Long) = db.getRemedies(userId)
        .map { it.mapToDomain() }
        .flowOn(dispatcher)

    override suspend fun getRemediesByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getRemediesByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getRemedyById(remedyId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getRemedyById(remedyId).mapToDomain()
        }
    }

    override suspend fun getRemedyByIdn(remedyIdn: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getRemedyByIdn(remedyIdn).mapToDomain()
        }
    }

    override suspend fun insertRemedy(remedy: RemedyDomainModel) = withContext(dispatcher) {
        Result.runCatching {
            db.insertRemedy(remedy.mapToData())
        }
    }

    override suspend fun insertRemedy(remedies: List<RemedyDomainModel>) = withContext(dispatcher) {
        Result.runCatching {
            db.insertRemedy(remedies.mapToData())
        }
    }

    override suspend fun updateRemedy(remedy: RemedyDomainModel) = withContext(dispatcher) {
        Result.runCatching {
            db.insertRemedy(remedy.mapToData())
        }
    }

    override suspend fun delete(remedyId: Long, dateTime: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.delete(remedyId, dateTime)
        }
    }

    override suspend fun deleteRemedyById(remedyId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.deleteRemedyById(remedyId)
        }
    }

    override suspend fun deleteRemedies(remedies: List<RemedyDomainModel>) =
        withContext(dispatcher) {
            Result.runCatching {
                db.deleteRemedy(remedies.mapToData())
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

    override suspend fun getRemedyDeletedByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getRemedyDeletedByUserId(userId).mapToDomain()
        }
    }
}
