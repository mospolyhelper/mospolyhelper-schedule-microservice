@file:Suppress("UnstableApiUsage")

plugins {
    application
    kotlin("jvm")
    id("pre-commit")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    applicationDefaultJvmArgs = listOf("-Xmx600m", "-Xss512k", "-XX:CICompilerCount=2")
}

kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

group = "com.mospolytech.microservices"
version = "com.mospolytech.microservices.0.0.1"


tasks.getByPath(":${project.path}:classes").dependsOn("installGitHook")
tasks.check.orNull?.dependsOn("ktlint")
tasks.create("stage") {
    dependsOn("installDist")
}