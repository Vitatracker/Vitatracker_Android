package app.mybad.network.models

import com.google.gson.Gson
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object AuthToken {
    var token = ""

    @OptIn(ExperimentalEncodingApi::class)
    val userId: Long
        get() = if (token.isNotBlank()) {
            val b2 = token.split('.')[1]
            val body = Base64.UrlSafe.decode(b2).decodeToString()
            val gson = Gson()
            gson.fromJson(body, Map::class.java)["id"].toString().toLongOrNull() ?: -1L
        } else -1L

    fun clear() {
        token = ""
    }

}
