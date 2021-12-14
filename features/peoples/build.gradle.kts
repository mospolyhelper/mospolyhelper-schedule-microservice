kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    api(project(":features:base"))
    api(project(":data:peoples"))
    api(project(":domain:peoples"))
}