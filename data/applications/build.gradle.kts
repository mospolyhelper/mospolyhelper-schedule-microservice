kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    api(projects.data.base)
    api(projects.domain.applications)
}
