package com.example.serialization

import com.example.serialization.kmodel.KDataset
import com.example.serialization.kmodel.KOrder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.modules.serializersModuleOf
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
class KotlinProtobufSerializationTest : AbstractSerializationTest<KOrder>() {

    private val protoBuf = ProtoBuf {
        serializersModule = serializersModuleOf(ByteArray::class, ByteArraySerializer())
    }

    override fun createDataset(): List<KOrder> = KDataset.create()

    override fun frameworkName() = "kotlinx-protobuf"

    override fun serialize(order: KOrder): ByteArray =
        protoBuf.encodeToByteArray(KOrder.serializer(), order)

    override fun deserialize(data: ByteArray): KOrder =
        protoBuf.decodeFromByteArray(KOrder.serializer(), data)
}
