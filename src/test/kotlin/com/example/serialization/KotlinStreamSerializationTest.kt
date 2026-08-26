@file:OptIn(ExperimentalSerializationApi::class)

package com.example.serialization

import com.example.serialization.kmodel.Base64ByteArraySerializer
import com.example.serialization.kmodel.KDataset
import com.example.serialization.kmodel.KOrder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.modules.serializersModuleOf
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class KotlinStreamSerializationTest : AbstractSerializationTest<KOrder>() {

    private val json = Json {
        serializersModule = serializersModuleOf(ByteArray::class, Base64ByteArraySerializer)
    }

    private val output = ByteArrayOutputStream(64 * 1024)

    override fun createDataset(): List<KOrder> = KDataset.create()

    override fun frameworkName() = "kotlinx-stream"

    override fun serialize(order: KOrder): ByteArray {
        output.reset()
        json.encodeToStream(KOrder.serializer(), order, output)
        return output.toByteArray()
    }

    override fun deserialize(data: ByteArray): KOrder =
        json.decodeFromStream(KOrder.serializer(), ByteArrayInputStream(data))
}
