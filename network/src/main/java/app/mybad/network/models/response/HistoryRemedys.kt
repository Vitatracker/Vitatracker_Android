package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class HistoryRemedys(
    @SerializedName("id") val id: Long,
    @SerializedName("remedyId") val remedyId: Long,
    @SerializedName("dose") val dose: Long,
    @SerializedName("measureUnit") val measureUnit: Int
)
