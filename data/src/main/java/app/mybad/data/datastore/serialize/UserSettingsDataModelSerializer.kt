package app.mybad.data.datastore.serialize

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.mybad.data.UserDataModel
import app.mybad.data.UserSettingsDataModel
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserSettingsDataModelSerializer : Serializer<UserSettingsDataModel> {

    override val defaultValue: UserSettingsDataModel = UserSettingsDataModel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSettingsDataModel {
        try {
            return UserSettingsDataModel.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserSettingsDataModel, output: OutputStream) {
        t.writeTo(output)
    }

}