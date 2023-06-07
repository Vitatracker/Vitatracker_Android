package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class Usages(
    @SerializedName("id") val id: Long,
    @SerializedName("courseId") val courseId: Long,
    @SerializedName("useTime") val useTime: Long,
    @SerializedName("factUseTime") val factUseTime: Long,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("notUsed") val notUsed: Boolean
)
