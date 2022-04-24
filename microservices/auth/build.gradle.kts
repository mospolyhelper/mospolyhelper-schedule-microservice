plugins {
    id("microservice-base")
}

version = "0.0.1"

dependencies {
    implementation(project(Modules.Data.Personal))
    implementation(project(Modules.Data.Auth))
    implementation(project(Modules.Domain.Auth))
    implementation(project(Modules.Features.Auth))
}