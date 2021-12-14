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
    implementation(project(":data:base"))
    implementation(project(":data:performance"))
    implementation(project(":data:peoples"))
    implementation(project(":data:applications"))
    implementation(project(":data:payments"))
    implementation(project(":data:personal"))
    implementation(project(":domain:base"))
    implementation(project(":domain:performance"))
    implementation(project(":domain:peoples"))
    implementation(project(":domain:applications"))
    implementation(project(":domain:payments"))
    implementation(project(":domain:personal"))
    implementation(project(":features:base"))
    implementation(project(":features:performance"))
    implementation(project(":features:peoples"))
    implementation(project(":features:applications"))
    implementation(project(":features:payments"))
    implementation(project(":features:personal"))
}