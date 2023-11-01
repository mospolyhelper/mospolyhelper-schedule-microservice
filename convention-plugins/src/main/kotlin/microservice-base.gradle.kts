@file:Suppress("UnstableApiUsage")
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("io.ktor.plugin")
    id("kotlin-base")
    id("pre-commit")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment", "-Xmx600m", "-Xss512k", "-XX:CICompilerCount=2")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

dependencies {
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.data.conversion)

    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
}

group = "com.mospolytech.microservices"
version = "com.mospolytech.microservices.0.0.1"


tasks.getByPath(":${project.path}:classes").dependsOn("installGitHook")
tasks.check.orNull?.dependsOn("ktlint")
tasks.create("stage") {
    dependsOn("installDist")
}
