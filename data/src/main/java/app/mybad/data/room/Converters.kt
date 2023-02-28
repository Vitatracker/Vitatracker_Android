package app.mybad.data.room

import androidx.room.TypeConverter
import app.mybad.data.models.med.MedDetailsDataModel
import app.mybad.data.models.usages.UsageDataModel
import app.mybad.data.models.usages.UsagesDataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun medDetailsToString(medDetailsDataModel: MedDetailsDataModel) =
        gson.toJson(medDetailsDataModel)

    @TypeConverter
    fun stringToMedDetails(str: String) : MedDetailsDataModel {
        return gson.fromJson(str, MedDetailsDataModel::class.java)
    }

    @TypeConverter
    fun usagesToString(usage: List<UsageDataModel>) =
        gson.toJson(usage)

    @TypeConverter
    fun stringToUsage(str: String) : List<UsageDataModel> {
        val type = object : TypeToken<List<UsageDataModel>>(){}.type
        return gson.fromJson(str, type)
    }

}