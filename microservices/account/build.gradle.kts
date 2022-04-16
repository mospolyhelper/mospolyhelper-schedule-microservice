plugins {
    id("microservice-base")
}

version = "0.0.1"

dependencies {
    implementation(project(Modules.Data.Performance))
    implementation(project(Modules.Data.Peoples))
    implementation(project(Modules.Data.Applications))
    implementation(project(Modules.Data.Payments))
    implementation(project(Modules.Data.Personal))


    implementation(project(Modules.Features.Performance))
    implementation(project(Modules.Features.Peoples))
    implementation(project(Modules.Features.Applications))
    implementation(project(Modules.Features.Payments))
    implementation(project(Modules.Features.Personal))
}