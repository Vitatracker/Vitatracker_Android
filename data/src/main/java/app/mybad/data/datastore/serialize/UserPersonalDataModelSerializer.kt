package app.mybad.data.datastore.serialize

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.vitatracker.data.UserPersonalDataModel
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserPersonalDataModelSerializer : Serializer<UserPersonalDataModel> {

    override val defaultValue: UserPersonalDataModel = UserPersonalDataModel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPersonalDataModel {
        try {
            return UserPersonalDataModel.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPersonalDataModel, output: OutputStream) {
        t.writeTo(output)
    }
}
