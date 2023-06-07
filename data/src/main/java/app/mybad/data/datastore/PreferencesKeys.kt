package app.mybad.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {

    val token = stringPreferencesKey("token")
    val userId = stringPreferencesKey("userId")

}
