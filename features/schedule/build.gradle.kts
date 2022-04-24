plugins {
    id("feature-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(project(Modules.Features.Base))
    api(project(Modules.Domain.Schedule))
    implementation(project(Modules.Domain.Personal))
}