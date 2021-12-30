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
    implementation(project(":data:auth"))
    implementation(project(":domain:auth"))
    implementation(project(":features:auth"))
}