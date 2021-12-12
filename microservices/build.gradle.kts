subprojects {
    group = "com.mospolytech.microservices"
    version = "com.mospolytech.microservices.0.0.1"

    tasks.create("stage") {
        dependsOn("installDist")
    }
}