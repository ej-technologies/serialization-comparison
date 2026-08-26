package com.example.serialization

import com.example.serialization.kmodel.KDataset
import com.example.serialization.kmodel.KOrder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class JacksonKotlinSerializationTest : AbstractSerializationTest<KOrder>() {

    private val mapper = ObjectMapper().registerKotlinModule()

    override fun createDataset(): List<KOrder> = KDataset.create()

    override fun frameworkName() = "jackson-kotlin"

    override fun serialize(order: KOrder): ByteArray = mapper.writeValueAsBytes(order)

    override fun deserialize(data: ByteArray): KOrder = mapper.readValue(data, KOrder::class.java)
}
