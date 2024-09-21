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
    implementation(libs.ktor.client.core.jvm)
    implementation(libs.ktor.client.apache)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(libs.logback)
    implementation(libs.quartz)
    implementation(libs.postgresql)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.java.time)

    implementation(libs.xmlutil.core)
    implementation(libs.xmlutil.serialization)

    implementation(libs.kotlinx.datetime)

    testImplementation(libs.kotlin.test.junit)
}

group = "com.mospolytech.data"
version = "com.mospolytech.data"
