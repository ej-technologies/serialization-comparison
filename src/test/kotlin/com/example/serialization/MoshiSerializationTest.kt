package com.example.serialization

import com.example.serialization.kmodel.KDataset
import com.example.serialization.kmodel.KOrder
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import okio.Buffer
import java.math.BigDecimal
import java.util.Base64
import java.util.Date

object BigDecimalAdapter {
    @ToJson
    fun toJson(value: BigDecimal): String = value.toPlainString()

    @FromJson
    fun fromJson(value: String): BigDecimal = BigDecimal(value)
}

object DateAdapter {
    @ToJson
    fun toJson(value: Date): Long = value.time

    @FromJson
    fun fromJson(value: Long): Date = Date(value)
}

object ByteArrayAdapter {
    @ToJson
    fun toJson(value: ByteArray): String = Base64.getEncoder().encodeToString(value)

    @FromJson
    fun fromJson(value: String): ByteArray = Base64.getDecoder().decode(value)
}

class MoshiSerializationTest : AbstractSerializationTest<KOrder>() {

    private val adapter = Moshi.Builder()
        .add(BigDecimalAdapter)
        .add(DateAdapter)
        .add(ByteArrayAdapter)
        .build()
        .adapter(KOrder::class.java)

    private val buffer = Buffer()

    override fun createDataset(): List<KOrder> = KDataset.create()

    override fun frameworkName() = "moshi"

    override fun serialize(order: KOrder): ByteArray {
        buffer.clear()
        adapter.toJson(buffer, order)
        return buffer.readByteArray()
    }

    override fun deserialize(data: ByteArray): KOrder =
        adapter.fromJson(Buffer().write(data))!!
}
