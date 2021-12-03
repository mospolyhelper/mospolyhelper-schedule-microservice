subprojects {
    group = "com.mospolytech.mph.microservices"
    version = "com.mospolytech.mph.microservices.0.0.1"

    tasks.create("stage") {
        dependsOn("installDist")
    }
}