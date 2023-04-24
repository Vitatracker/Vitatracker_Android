package app.mybad.data.datastore

interface DataStorePref {
    suspend fun updateToken(token: String)
    suspend fun getToken(): String
}
