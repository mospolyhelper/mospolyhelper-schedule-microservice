plugins {
    id("feature-base")
}

dependencies {
    api(project(Modules.Features.Base))
    api(project(Modules.Domain.Schedule))
    implementation(project(Modules.Domain.Personal))
}