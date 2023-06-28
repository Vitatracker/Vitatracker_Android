package app.mybad.domain.usecases.settings

import app.mybad.domain.repos.SettingsNetworkRepository
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val repository: SettingsNetworkRepository,
) {

    suspend operator fun invoke() = repository.getUserModel()

}
