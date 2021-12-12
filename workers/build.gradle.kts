subprojects {
    group = "com.mospolytech.workers"
    version = "com.mospolytech.workers.0.0.1"

    tasks.create("stage") {
        dependsOn("installDist")
    }
}