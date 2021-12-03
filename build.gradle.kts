plugins {
    application
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
    }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometeus_version : String by project

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    dependencies {
        implementation("io.ktor:ktor-server-auth:$ktor_version")
        implementation("io.ktor:ktor-server-core:$ktor_version")
        implementation("io.ktor:ktor-locations:$ktor_version")
        implementation("io.ktor:ktor-client-core:$ktor_version")
        implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
        implementation("io.ktor:ktor-client-apache:$ktor_version")
        implementation("io.ktor:ktor-server-host-common:$ktor_version")
        implementation("io.ktor:ktor-server-status-pages:$ktor_version")
        implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
        implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
        implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
        implementation("io.micrometer:micrometer-registry-prometheus:$prometeus_version")
        implementation("io.ktor:ktor-metrics:$ktor_version")
        implementation("io.ktor:ktor-server-call-logging:$ktor_version")
        implementation("io.ktor:ktor-server-cors:$ktor_version")
        implementation("io.ktor:ktor-server-netty:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        testImplementation("io.ktor:ktor-server-tests:$ktor_version")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    }
}