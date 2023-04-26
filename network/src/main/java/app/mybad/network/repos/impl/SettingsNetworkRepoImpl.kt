package app.mybad.network.repos.impl

import app.mybad.network.api.SettingsApiRepo
import app.mybad.network.repos.repo.SettingsNetworkRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsNetworkRepoImpl @Inject constructor(
    private val settingsApiRepo: SettingsApiRepo
) : SettingsNetworkRepo {
}