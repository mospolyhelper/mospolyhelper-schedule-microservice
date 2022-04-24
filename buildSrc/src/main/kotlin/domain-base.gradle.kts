@file:Suppress("UnstableApiUsage")

plugins {
    kotlin("jvm")
}

kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

group = "com.mospolytech.domain"
version = "com.mospolytech.domain"