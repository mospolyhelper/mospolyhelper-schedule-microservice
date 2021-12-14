kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    api(project(":features:base"))
    api(project(":data:personal"))
    api(project(":domain:personal"))
}