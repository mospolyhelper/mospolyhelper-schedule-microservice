@file:Suppress("UnstableApiUsage")
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("kotlin-base")
    kotlin("plugin.serialization")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

dependencies {
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.ktor.client.core)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.kotlin.test.junit)
}

group = "com.mospolytech.domain"
version = "com.mospolytech.domain"
