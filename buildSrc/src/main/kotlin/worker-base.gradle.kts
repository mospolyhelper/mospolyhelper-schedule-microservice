@file:Suppress("UnstableApiUsage")

plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    applicationDefaultJvmArgs = listOf("-Xmx300m", "-Xss512k", "-XX:CICompilerCount=2")
}

kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

group = "com.mospolytech.workers"
version = "com.mospolytech.workers.0.0.1"

tasks.create("stage") {
    dependsOn("installDist")
}