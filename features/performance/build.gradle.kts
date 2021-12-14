kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    api(project(":features:base"))
    api(project(":data:performance"))
    api(project(":domain:performance"))
}