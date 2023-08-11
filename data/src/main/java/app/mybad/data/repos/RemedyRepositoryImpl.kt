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
        runCatching {
            db.getRemediesByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getRemedyById(remedyId: Long) = withContext(dispatcher) {
        runCatching {
            db.getRemedyById(remedyId).mapToDomain()
        }
    }

    override suspend fun getRemedyByIdn(remedyIdn: Long) = withContext(dispatcher) {
        runCatching {
            db.getRemedyByIdn(remedyIdn).mapToDomain()
        }
    }

    override suspend fun getRemediesByIds(remedyIdList: List<Long>) = withContext(dispatcher) {
        runCatching {
            db.getRemediesByIds(remedyIdList).mapToDomain()
        }
    }

    override suspend fun insertRemedy(remedy: RemedyDomainModel) = withContext(dispatcher) {
        runCatching {
            db.insertRemedy(remedy.mapToData())
        }
    }

    override suspend fun insertRemedy(remedies: List<RemedyDomainModel>) = withContext(dispatcher) {
        runCatching {
            db.insertRemedy(remedies.mapToData())
        }
    }

    override suspend fun updateRemedy(remedy: RemedyDomainModel) = withContext(dispatcher) {
        runCatching {
            db.insertRemedy(remedy.mapToData())
        }
    }

    override suspend fun markDeletionRemedyById(remedyId: Long) = withContext(dispatcher) {
        runCatching {
            db.markDeletionRemedyById(remedyId)
        }
    }

    override suspend fun deleteRemedyById(remedyId: Long) = withContext(dispatcher) {
        runCatching {
            db.deleteRemedyById(remedyId)
        }
    }

    override suspend fun deleteRemediesByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.deleteRemediesByUserId(userId)
        }
    }

    override suspend fun deleteRemedies(remedies: List<RemedyDomainModel>) =
        withContext(dispatcher) {
            runCatching {
                db.deleteRemedies(remedies.mapToData())
            }
        }

    override suspend fun getRemedyNotUpdateByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getRemedyNotUpdateByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getRemedyDeletedByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getRemedyDeletedByUserId(userId).mapToDomain()
        }
    }
}
