package com.example.helper.domen.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

@kotlinx.serialization.Serializable
data class Relative(
    @SerializedName("id")
    val id:String?=null,
    @SerializedName("name")
    var name:String?=null,
    @SerializedName("surname")
    var surname:String?=null,
    @SerializedName("secondName")
    var secondName:String?=null,
    @SerializedName("gender")
    @kotlinx.serialization.Serializable(EGenderSerializer::class)
    var gender:EGender?=null,
    @SerializedName("birthDate")
//    @Serializable(DateSerializer::class)
    var birthDate:String?=null,
    @SerializedName("birthPlace")
    var birthPlace:String?=null,
    @SerializedName("placeOfLiving")
    var placeOfLiving:String?=null,
    @SerializedName("photo")
    var photo:String?=null,
    @SerializedName("fatherId")
    var fatherId:String?=null,
    @SerializedName("motherId")
    var motherId:String?=null,
    @SerializedName("link")
    var link:String?=null,
    @SerializedName("livingStatus")
//    @kotlinx.serialization.Serializable(LivingStatusSerializer::class)
    var livingStatus:String?=null,
    @SerializedName("userId")
    var userId:String?=null,
    @SerializedName("relativeDoc")
    var relativeDoc:String?=null
)

enum class EGender(val id:Int) {
    FEMALE(1),
    MALE(2);

    companion object{
        fun valueOf(value:Int) = values().find { it.id == value }?:throw NullPointerException()
    }
}

enum class LivingStatus(val id:Int) {
    Alive(1),
    Deceased(2);

    companion object{
        fun valueOf(value:Int) = values().find { it.id == value }?:throw NullPointerException()
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DateSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.time.toString())
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeString())
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LivingStatus::class)
object LivingStatusSerializer : KSerializer<LivingStatus> {
    override fun serialize(encoder: Encoder, value: LivingStatus) {
        encoder.encodeString(value.toString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LivingStatusSerializer",PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LivingStatus {
        return LivingStatus.valueOf(decoder.decodeString())
    }
}
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = EGender::class)
object EGenderSerializer : KSerializer<EGender> {
    override fun serialize(encoder: Encoder, value: EGender) {
        encoder.encodeString(value.toString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("EGenderSerializer",PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): EGender {
        return EGender.valueOf(decoder.decodeString())
    }
}