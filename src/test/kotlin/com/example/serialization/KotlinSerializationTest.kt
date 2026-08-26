package com.example.serialization

import com.example.serialization.kmodel.Base64ByteArraySerializer
import com.example.serialization.kmodel.KDataset
import com.example.serialization.kmodel.KOrder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf

class KotlinSerializationTest : AbstractSerializationTest<KOrder>() {

    private val json = Json {
        serializersModule = serializersModuleOf(ByteArray::class, Base64ByteArraySerializer)
    }

    override fun createDataset(): List<KOrder> = KDataset.create()

    override fun frameworkName() = "kotlinx"

    override fun serialize(order: KOrder): ByteArray =
        json.encodeToString(KOrder.serializer(), order).toByteArray(Charsets.UTF_8)

    override fun deserialize(data: ByteArray): KOrder =
        json.decodeFromString(KOrder.serializer(), String(data, Charsets.UTF_8))
}
