plugins {
    id("feature-base")
}

dependencies {
    api(project(Modules.Features.Base))
    api(project(Modules.Domain.Payments))
}