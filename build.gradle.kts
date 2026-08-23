import com.jprofiler.buildtools.CallTreeMode
import com.jprofiler.gradle.TestProfile

plugins {
    java
    id("com.jprofiler") version "16.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.fory:fory-core:1.6.0")
    implementation("com.esotericsoftware:kryo5:5.6.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:2.22.1")
    implementation("com.fasterxml.jackson.module:jackson-module-afterburner:2.22.1")

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
    "Kryo" to "com.example.serialization.KryoSerializationTest",
    "Jackson" to "com.example.serialization.JacksonSerializationTest",
    "JacksonCbor" to "com.example.serialization.JacksonCborSerializationTest"
)

fun profileTask(name: String, testClass: String, recording: String) {
    tasks.create<TestProfile>("profile$name$recording") {
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
