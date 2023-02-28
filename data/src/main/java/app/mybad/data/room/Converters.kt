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
        gson.toJson(medDetailsDataModel).toString()

    @TypeConverter
    fun stringToMedDetails(str: String) : MedDetailsDataModel {
        return gson.fromJson(str, MedDetailsDataModel::class.java)
    }

    @TypeConverter
    fun usagesToString(usages: List<UsageDataModel>) =
        gson.toJson(usages)

    @TypeConverter
    fun stringToUsage(str: String) : List<UsageDataModel> {
        val type = object : TypeToken<List<UsageDataModel>>(){}.type
        return gson.fromJson(str, type)
    }
    @TypeConverter
    fun singleUsageToString(usage: UsageDataModel) : String =
        gson.toJson(usage)

    @TypeConverter
    fun stringToSingleUsage(str: String) : UsageDataModel =
        gson.fromJson(str, UsageDataModel::class.java)

    @TypeConverter
    fun usagesModelToString(usage: UsagesDataModel) =
        gson.toJson(usage).toString()

    @TypeConverter
    fun stringToUsagesModel(str: String) : UsagesDataModel {
        return gson.fromJson(str, UsagesDataModel::class.java)
    }

}