package app.mybad.network.repository

import app.mybad.domain.repository.network.UsageNetworkRepository
import app.mybad.network.api.UsageApi
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class UsageNetworkRepositoryImpl @Inject constructor(
    private val usageApi: UsageApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : UsageNetworkRepository {
}
