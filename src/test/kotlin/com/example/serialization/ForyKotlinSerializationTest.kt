package com.example.serialization

import com.example.serialization.kmodel.KDataset
import com.example.serialization.kmodel.KOrder
import org.apache.fory.Fory
import org.apache.fory.config.Language

class ForyKotlinSerializationTest : AbstractSerializationTest<KOrder>() {

    private val fory = Fory.builder()
        .withLanguage(Language.JAVA)
        .withRefTracking(true)
        .requireClassRegistration(false)
        .build()

    override fun createDataset(): List<KOrder> = KDataset.create()

    override fun frameworkName() = "fory-kotlin"

    override fun serialize(order: KOrder): ByteArray = fory.serialize(order)

    override fun deserialize(data: ByteArray): KOrder = fory.deserialize(data) as KOrder
}
