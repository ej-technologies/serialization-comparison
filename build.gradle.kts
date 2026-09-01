@file:Suppress("DEPRECATION_ERROR")

import com.jprofiler.buildtools.CallTreeMode
import com.jprofiler.gradle.TestProfile

plugins {
    java
    kotlin("jvm") version "2.4.10"
    kotlin("plugin.serialization") version "2.4.10"
    id("com.google.devtools.ksp") version "2.3.11"
    id("com.jprofiler") version "16.2.1"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.apache.fory:fory-core:1.7.0")
    implementation("org.apache.fory:fory-json:1.7.0")
    implementation("com.esotericsoftware:kryo5:5.6.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:2.22.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.11.0")
    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.22.1")
    implementation("com.fasterxml.jackson.module:jackson-module-afterburner:2.22.1")
    implementation("tools.jackson.core:jackson-databind:3.2.2")
    implementation("tools.jackson.dataformat:jackson-dataformat-cbor:3.2.2")
    implementation("tools.jackson.module:jackson-module-kotlin:3.2.2")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

jprofiler {
    findProperty("jprofilerInstallDir")?.let { installDir = file(it) }
}

tasks.test {
    useJUnitPlatform()
    systemProperty("iterations", providers.gradleProperty("iterations").orElse("200").get())
}

val frameworks = mapOf(
    "Java" to "com.example.serialization.JavaSerializationTest",
    "Fory" to "com.example.serialization.ForySerializationTest",
    "ForyJson" to "com.example.serialization.ForyJsonSerializationTest",
    "Kryo" to "com.example.serialization.KryoSerializationTest",
    "Jackson" to "com.example.serialization.JacksonSerializationTest",
    "JacksonCbor" to "com.example.serialization.JacksonCborSerializationTest",
    "Jackson3" to "com.example.serialization.Jackson3SerializationTest",
    "Jackson3Cbor" to "com.example.serialization.Jackson3CborSerializationTest",
    "Jackson3Kotlin" to "com.example.serialization.Jackson3KotlinSerializationTest",
    "Kotlin" to "com.example.serialization.KotlinSerializationTest",
    "KotlinStream" to "com.example.serialization.KotlinStreamSerializationTest",
    "KotlinCbor" to "com.example.serialization.KotlinCborSerializationTest",
    "KotlinProtobuf" to "com.example.serialization.KotlinProtobufSerializationTest",
    "Moshi" to "com.example.serialization.MoshiSerializationTest",
    "JacksonKotlin" to "com.example.serialization.JacksonKotlinSerializationTest",
    "MoshiJava" to "com.example.serialization.MoshiJavaSerializationTest",
    "ForyKotlin" to "com.example.serialization.ForyKotlinSerializationTest"
)

fun profileTask(name: String, testClass: String, recording: String) {
    tasks.register<TestProfile>("profile$name$recording") {
        useJUnitPlatform()
        maxHeapSize = "2g"
        filter {
            includeTestsMatching(testClass)
        }
        testLogging.showStandardStreams = true
        systemProperty("iterations", providers.gradleProperty("iterations").orElse("2000000").get())
        offline = true
        callTreeMode = CallTreeMode.SAMPLING
        this.recording = recording
        profile = listOf("com.", "org.", "java.", "javax.", "jdk.", "sun.", "io.", "net.", "worker.")
        val file = layout.buildDirectory.file("snapshots-$recording/${name.lowercase()}.jps").get().asFile
        snapshotFile = file
        doFirst {
            file.parentFile.mkdirs()
        }
    }
}

frameworks.forEach { (name, testClass) ->
    profileTask(name, testClass,"cpu")
    profileTask(name, testClass,"allocation")
}
