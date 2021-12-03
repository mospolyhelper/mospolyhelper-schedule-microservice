group = "com.mospolytech.mph.schedule"
version = "0.0.1"

kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    implementation(project(":data:base"))
    implementation(project(":data:schedule"))
    implementation(project(":domain:base"))
    implementation(project(":domain:schedule"))
    implementation(project(":features:schedule"))
}