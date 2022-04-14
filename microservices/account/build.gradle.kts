plugins {
    id("microservice-base")
}

version = "0.0.1"

dependencies {
    implementation(project(Modules.Features.Base))
    implementation(project(Modules.Features.Performance))
    implementation(project(Modules.Features.Peoples))
    implementation(project(Modules.Features.Applications))
    implementation(project(Modules.Features.Payments))
    implementation(project(Modules.Features.Personal))
}