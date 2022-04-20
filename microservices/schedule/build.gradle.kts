plugins {
    id("microservice-base")
}

version = "0.0.2"

dependencies {
    implementation(project(Modules.Data.Base))
    implementation(project(Modules.Data.Auth))
    implementation(project(Modules.Data.Schedule))
    implementation(project(Modules.Data.Personal))

    implementation(project(Modules.Domain.Base))
    implementation(project(Modules.Domain.Auth))
    implementation(project(Modules.Domain.Schedule))

    implementation(project(Modules.Features.Auth))
    implementation(project(Modules.Features.Schedule))
}