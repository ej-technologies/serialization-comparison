@file:OptIn(ExperimentalSerializationApi::class)

package com.example.serialization

import com.example.serialization.kmodel.Base64ByteArraySerializer
import com.example.serialization.kmodel.KDataset
import com.example.serialization.kmodel.KOrder
import com.example.serialization.model.Order
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.modules.serializersModuleOf
import okio.Buffer
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class JsonRoundTripBenchmark {
    private val serializer = KOrder.serializer()
    private val kotlinxJson = Json {
        serializersModule = serializersModuleOf(ByteArray::class, Base64ByteArraySerializer)
    }
    private val kotlinxStreamOutput = ByteArrayOutputStream(64 * 1024)

    private val jacksonJava = ObjectMapper()
    private val jacksonKotlin = ObjectMapper().registerKotlinModule()

    private val moshi = Moshi.Builder()
        .add(JmhJsonAdapters())
        .build()
    private val moshiKotlin: JsonAdapter<KOrder> = moshi.adapter(KOrder::class.java)
    private val moshiJava: JsonAdapter<Order> = moshi.adapter(Order::class.java)
    private val moshiKotlinOutput = Buffer()
    private val moshiJavaOutput = Buffer()

    private lateinit var kotlinOrders: List<KOrder>
    private lateinit var javaOrders: List<Order>
    private var kotlinIndex = 0
    private var javaIndex = 0

    @Setup(Level.Trial)
    open fun setup() {
        kotlinOrders = KDataset.create()
        javaOrders = DatasetGenerator.create()
    }

    private fun nextKotlinOrder(): KOrder {
        val order = kotlinOrders[kotlinIndex]
        kotlinIndex = (kotlinIndex + 1) % kotlinOrders.size
        return order
    }

    private fun nextJavaOrder(): Order {
        val order = javaOrders[javaIndex]
        javaIndex = (javaIndex + 1) % javaOrders.size
        return order
    }

    @Benchmark
    open fun kotlinxString(): KOrder {
        val encoded = kotlinxJson.encodeToString(serializer, nextKotlinOrder())
        return kotlinxJson.decodeFromString(serializer, encoded)
    }

    @Benchmark
    open fun kotlinxStream(): KOrder {
        kotlinxStreamOutput.reset()
        kotlinxJson.encodeToStream(serializer, nextKotlinOrder(), kotlinxStreamOutput)
        val encoded = kotlinxStreamOutput.toByteArray()
        return kotlinxJson.decodeFromStream(serializer, ByteArrayInputStream(encoded))
    }

    @Benchmark
    open fun jacksonKotlin(): KOrder {
        val encoded = jacksonKotlin.writeValueAsBytes(nextKotlinOrder())
        return jacksonKotlin.readValue(encoded, KOrder::class.java)
    }

    @Benchmark
    open fun jacksonJava(): Order {
        val encoded = jacksonJava.writeValueAsBytes(nextJavaOrder())
        return jacksonJava.readValue(encoded, Order::class.java)
    }

    @Benchmark
    open fun moshiKotlin(): KOrder {
        moshiKotlinOutput.clear()
        moshiKotlin.toJson(moshiKotlinOutput, nextKotlinOrder())
        val encoded = moshiKotlinOutput.readByteArray()
        return moshiKotlin.fromJson(Buffer().write(encoded))!!
    }

    @Benchmark
    open fun moshiJava(): Order {
        moshiJavaOutput.clear()
        moshiJava.toJson(moshiJavaOutput, nextJavaOrder())
        val encoded = moshiJavaOutput.readByteArray()
        return moshiJava.fromJson(Buffer().write(encoded))!!
    }
}
