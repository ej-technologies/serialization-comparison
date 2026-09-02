package com.example.serialization

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal
import java.util.Base64
import java.util.Date

class JmhJsonAdapters {
    @ToJson
    fun bigDecimalToJson(value: BigDecimal): String = value.toPlainString()

    @FromJson
    fun bigDecimalFromJson(value: String): BigDecimal = BigDecimal(value)

    @ToJson
    fun dateToJson(value: Date): Long = value.time

    @FromJson
    fun dateFromJson(value: Long): Date = Date(value)

    @ToJson
    fun byteArrayToJson(value: ByteArray): String = Base64.getEncoder().encodeToString(value)

    @FromJson
    fun byteArrayFromJson(value: String): ByteArray = Base64.getDecoder().decode(value)
}
