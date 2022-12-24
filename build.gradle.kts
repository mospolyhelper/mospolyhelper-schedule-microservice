plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometeus_version: String by project
val koin_version: String by project
val exposed_version: String by project
val xmlutil_version: String by project
val kotlinx_datetime: String by project
val postgresql: String by project
val quartzVersion: String by project

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xuse-experimental=io.ktor.server.locations.KtorExperimentalLocationsAPI",
        )
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "ktlint")

    dependencies {
        implementation("io.ktor:ktor-server-auth:$ktor_version")
        implementation("io.ktor:ktor-server-core:$ktor_version")
        implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
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
        implementation("io.ktor:ktor-server-websockets:$ktor_version")


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
        implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
        implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

        implementation("io.github.pdvrieze.xmlutil:core:$xmlutil_version")
        implementation("io.github.pdvrieze.xmlutil:serialization:$xmlutil_version")

        implementation("org.postgresql:postgresql:$postgresql")

        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinx_datetime")

        implementation("org.quartz-scheduler:quartz:$quartzVersion")


        testImplementation("io.ktor:ktor-server-tests:$ktor_version")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    }
}
