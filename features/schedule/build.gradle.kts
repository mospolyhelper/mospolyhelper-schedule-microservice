kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    api(project(":data:schedule"))
    api(project(":domain:schedule"))
}