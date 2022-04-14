plugins {
    id("worker-base")
}

version = "0.0.1"

dependencies {
    implementation(project(Modules.Data.Base))
    implementation(project(Modules.Data.Schedule))
    implementation(project(Modules.Domain.Base))
    implementation(project(Modules.Domain.Schedule))
    implementation(project(Modules.Features.Schedule))
}