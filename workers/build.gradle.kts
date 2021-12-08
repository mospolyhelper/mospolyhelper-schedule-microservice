subprojects {
    group = "com.mospolytech.mph.workers"
    version = "com.mospolytech.mph.workers.0.0.1"

    tasks.create("stage") {
        dependsOn("installDist")
    }
}