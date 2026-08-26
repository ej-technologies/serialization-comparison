package com.example.serialization

import com.example.serialization.kmodel.KDataset
import com.example.serialization.kmodel.KOrder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.modules.serializersModuleOf

@OptIn(ExperimentalSerializationApi::class)
class KotlinCborSerializationTest : AbstractSerializationTest<KOrder>() {

    private val cbor = Cbor {
        serializersModule = serializersModuleOf(ByteArray::class, ByteArraySerializer())
    }

    override fun createDataset(): List<KOrder> = KDataset.create()

    override fun frameworkName() = "kotlinx-cbor"

    override fun serialize(order: KOrder): ByteArray =
        cbor.encodeToByteArray(KOrder.serializer(), order)

    override fun deserialize(data: ByteArray): KOrder =
        cbor.decodeFromByteArray(KOrder.serializer(), data)
}
