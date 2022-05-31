plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometeus_version: String by project
val koin_version: String by project
val exposed_version: String by project

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xuse-experimental=io.ktor.server.locations.KtorExperimentalLocationsAPI",
        )
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    dependencies {
        implementation("io.ktor:ktor-server-auth:$ktor_version")
        implementation("io.ktor:ktor-server-core:$ktor_version")
        implementation("io.ktor:ktor-server-host-common:$ktor_version")
        implementation("io.ktor:ktor-server-status-pages:$ktor_version")
        implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
        implementation("io.ktor:ktor-server-data-conversion:$ktor_version")
        implementation("io.ktor:ktor-server-call-logging:$ktor_version")
        implementation("io.ktor:ktor-server-cors:$ktor_version")
        implementation("io.ktor:ktor-server-netty:$ktor_version")
        implementation("io.ktor:ktor-server-locations:$ktor_version")
        implementation("io.ktor:ktor-server-auth:$ktor_version")
        implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")


        implementation("io.ktor:ktor-client-core:$ktor_version")
        implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
        implementation("io.ktor:ktor-client-apache:$ktor_version")
        implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
        implementation("io.ktor:ktor-client-logging:$ktor_version")


        implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
        implementation("io.ktor:ktor-server-metrics:$ktor_version")
        implementation("io.ktor:ktor-server-metrics-micrometer:$ktor_version")
        implementation("io.micrometer:micrometer-registry-prometheus:$prometeus_version")
        implementation("io.insert-koin:koin-core:$koin_version")
        implementation("io.insert-koin:koin-ktor:$koin_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")

        implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
        implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
        implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

        implementation("io.github.pdvrieze.xmlutil:core:0.84.2")
        implementation("io.github.pdvrieze.xmlutil:serialization:0.84.2")


        testImplementation("io.ktor:ktor-server-tests:$ktor_version")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    }
}