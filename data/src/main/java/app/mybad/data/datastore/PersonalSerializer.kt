package app.mybad.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.mybad.data.models.user.PersonalDataModel
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object PersonalSerializer : Serializer<PersonalDataModel> {

    override val defaultValue: PersonalDataModel
        get() = TODO("Not yet implemented")

    override suspend fun readFrom(input: InputStream): PersonalDataModel {
        try {
            return PersonalDataModel()
//            return PersonalDataModel.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: PersonalDataModel, output: OutputStream) {
//        t.writeTo(output)
    }

}