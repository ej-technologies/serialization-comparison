package com.example.serialization

import com.example.serialization.kmodel.KDataset
import com.example.serialization.kmodel.KOrder
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule

class Jackson3KotlinSerializationTest : AbstractSerializationTest<KOrder>() {

    private val mapper = JsonMapper.builder()
        .addModule(KotlinModule.Builder().build())
        .build()

    override fun createDataset(): List<KOrder> = KDataset.create()

    override fun frameworkName() = "jackson3-kotlin"

    override fun serialize(order: KOrder): ByteArray = mapper.writeValueAsBytes(order)

    override fun deserialize(data: ByteArray): KOrder = mapper.readValue(data, KOrder::class.java)
}
