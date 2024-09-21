plugins {
    id("com.mospolytech.lint")
    kotlin("jvm")
}

kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}
