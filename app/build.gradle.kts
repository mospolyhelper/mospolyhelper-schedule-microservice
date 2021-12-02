val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometeus_version : String by project

group = "com.mospolytech.mph.schedule"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation(project(":data:base"))
}