kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    api(project(":data:base"))
//    api(project(":domain:performance"))
}