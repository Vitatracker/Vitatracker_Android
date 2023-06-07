package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class Remedies(
    @SerializedName("id") val id: Long,
    @SerializedName("userid") val userId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("comment") val comment: String,
    @SerializedName("type") val type: Int,
    @SerializedName("icon") val icon: Long,
    @SerializedName("color") val color: Long,
    @SerializedName("dose") val dose: Long,
    @SerializedName("measureUnit") val measureUnit: Int,
    @SerializedName("beforeFood") val beforeFood: Int,
    @SerializedName("photo") val photo: String,
    @SerializedName("historyRemedys") val historyRemedys: List<HistoryRemedys>?,
    @SerializedName("courses") val courses: List<Courses>?
)
