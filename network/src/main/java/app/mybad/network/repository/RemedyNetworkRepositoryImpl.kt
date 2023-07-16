package app.mybad.network.repository

import app.mybad.domain.repository.network.RemedyNetworkRepository
import app.mybad.network.api.RemedyApi
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class RemedyNetworkRepositoryImpl @Inject constructor(
    private val remedyApi: RemedyApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : RemedyNetworkRepository {
}
