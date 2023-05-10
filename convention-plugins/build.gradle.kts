plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradlePlugin.kotlin.jvm)
    implementation(libs.gradlePlugin.ktlint)
    implementation(libs.gradlePlugin.kotlinxSerialization)
    implementation(libs.gradlePlugin.ktor)

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
