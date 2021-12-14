plugins {
    application
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    applicationDefaultJvmArgs = listOf("-Xmx300m", "-Xss512k", "-XX:CICompilerCount=2")
}

version = "0.0.1"

kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    implementation(project(":features:base"))
    implementation(project(":features:performance"))
    implementation(project(":features:peoples"))
    implementation(project(":features:applications"))
    implementation(project(":features:payments"))
    implementation(project(":features:personal"))
}