package app.mybad.data.datastore.serialize

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.mybad.data.UserNotificationsDataModel
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserNotificationDataModelSerializer : Serializer<UserNotificationsDataModel> {

    override val defaultValue: UserNotificationsDataModel = UserNotificationsDataModel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserNotificationsDataModel {
        try {
            return UserNotificationsDataModel.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserNotificationsDataModel, output: OutputStream) {
        t.writeTo(output)
    }

}