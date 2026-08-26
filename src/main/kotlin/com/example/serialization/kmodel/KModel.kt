@file:OptIn(ExperimentalSerializationApi::class)

package com.example.serialization.kmodel

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.ByteString
import kotlinx.serialization.Contextual
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import com.squareup.moshi.JsonClass
import kotlinx.serialization.protobuf.ProtoNumber
import java.math.BigDecimal
import java.util.Base64
import java.util.Date

object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: BigDecimal) = encoder.encodeString(value.toPlainString())
    override fun deserialize(decoder: Decoder): BigDecimal = BigDecimal(decoder.decodeString())
}

object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)
    override fun deserialize(decoder: Decoder): Date = Date(decoder.decodeLong())
}

object Base64ByteArraySerializer : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ByteArray", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: ByteArray) = encoder.encodeString(Base64.getEncoder().encodeToString(value))
    override fun deserialize(decoder: Decoder): ByteArray = Base64.getDecoder().decode(decoder.decodeString())
}

@JsonClass(generateAdapter = true)
@Serializable
data class KAddress(
    @ProtoNumber(1) val street: String,
    @ProtoNumber(2) val city: String,
    @ProtoNumber(3) val zip: String,
    @ProtoNumber(4) val country: String
)

@JsonClass(generateAdapter = true)
@Serializable
data class KCustomer(
    @ProtoNumber(1) val id: Long,
    @ProtoNumber(2) val firstName: String,
    @ProtoNumber(3) val lastName: String,
    @ProtoNumber(4) val email: String,
    @Serializable(with = DateSerializer::class) @ProtoNumber(5) val dateOfBirth: Date,
    @ProtoNumber(6) val vip: Boolean,
    @ProtoNumber(7) val addresses: List<KAddress>
)

@JsonClass(generateAdapter = true)
@Serializable
data class KCategory(
    @ProtoNumber(1) val id: Long,
    @ProtoNumber(2) val name: String,
    @ProtoNumber(3) val parent: KCategory? = null
)

@JsonClass(generateAdapter = true)
@Serializable
data class KProduct(
    @ProtoNumber(1) val sku: String,
    @ProtoNumber(2) val name: String,
    @ProtoNumber(3) val description: String,
    @ProtoNumber(4) val category: KCategory,
    @Serializable(with = BigDecimalSerializer::class) @ProtoNumber(5) val price: BigDecimal,
    @ProtoNumber(6) val weightGrams: Int,
    @ProtoNumber(7) val tags: Set<String>,
    @ByteString @Contextual @ProtoNumber(8) val thumbnail: ByteArray
) {
    override fun equals(other: Any?): Boolean =
        other is KProduct && sku == other.sku && name == other.name && description == other.description &&
                category == other.category && price == other.price && weightGrams == other.weightGrams &&
                tags == other.tags && thumbnail.contentEquals(other.thumbnail)

    override fun hashCode(): Int =
        31 * listOf(sku, name, description, category, price, weightGrams, tags).hashCode() + thumbnail.contentHashCode()
}

@JsonClass(generateAdapter = true)
@Serializable
data class KOrderLine(
    @ProtoNumber(1) val product: KProduct,
    @ProtoNumber(2) val quantity: Int,
    @Serializable(with = BigDecimalSerializer::class) @ProtoNumber(3) val unitPrice: BigDecimal,
    @ProtoNumber(4) val discount: Double
)

enum class KOrderStatus {
    NEW, PAID, PICKING, SHIPPED, DELIVERED, CANCELLED
}

@JsonClass(generateAdapter = true)
@Serializable
data class KOrder(
    @ProtoNumber(1) val id: String,
    @ProtoNumber(2) val customer: KCustomer,
    @ProtoNumber(3) val lines: List<KOrderLine>,
    @ProtoNumber(4) val status: KOrderStatus,
    @Serializable(with = DateSerializer::class) @ProtoNumber(5) val created: Date,
    @ProtoNumber(6) val shippingAddress: KAddress,
    @ProtoNumber(7) val attributes: Map<String, String>,
    @ProtoNumber(8) val notes: String?
)
