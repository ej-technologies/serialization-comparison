package com.example.serialization

import com.example.serialization.model.Order
import com.squareup.moshi.Moshi
import okio.Buffer

class MoshiJavaSerializationTest : AbstractSerializationTest<Order>() {

    private val adapter = Moshi.Builder()
        .add(BigDecimalAdapter)
        .add(DateAdapter)
        .add(ByteArrayAdapter)
        .build()
        .adapter(Order::class.java)

    private val buffer = Buffer()

    override fun createDataset(): List<Order> = DatasetGenerator.create()

    override fun frameworkName() = "moshi-java"

    override fun serialize(order: Order): ByteArray {
        buffer.clear()
        adapter.toJson(buffer, order)
        return buffer.readByteArray()
    }

    override fun deserialize(data: ByteArray): Order =
        adapter.fromJson(Buffer().write(data))!!
}
