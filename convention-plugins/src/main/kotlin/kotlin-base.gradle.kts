plugins {
    id("ktlint")
    kotlin("jvm")
}

kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}
