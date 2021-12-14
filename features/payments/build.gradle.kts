kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    api(project(":features:base"))
    api(project(":data:payments"))
    api(project(":domain:payments"))
}