package app.mybad.data.datastore.serialize

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.mybad.data.UserRulesDataModel
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserRulesDataModelSerializer : Serializer<UserRulesDataModel> {

    override val defaultValue: UserRulesDataModel = UserRulesDataModel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserRulesDataModel {
        try {
            return UserRulesDataModel.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserRulesDataModel, output: OutputStream) {
        t.writeTo(output)
    }

}