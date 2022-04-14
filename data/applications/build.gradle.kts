kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    api(project(Modules.Data.Base))
    api(project(Modules.Domain.Applications))
}