package app.mybad.data.datastore.serialize

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.mybad.data.UserDataModel
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserDataModelSerializer : Serializer<UserDataModel> {

    override val defaultValue: UserDataModel = UserDataModel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserDataModel {
        try {
            return UserDataModel.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserDataModel, output: OutputStream) {
        t.writeTo(output)
    }
}