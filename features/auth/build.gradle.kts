kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    api(project(":domain:auth"))
    api(project(":domain:personal"))
    api(project(":features:base"))
}