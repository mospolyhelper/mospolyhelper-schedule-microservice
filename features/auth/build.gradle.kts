plugins {
    id("feature-base")
}

dependencies {
    api(project(Modules.Domain.Auth))
    api(project(Modules.Domain.Personal))
    api(project(Modules.Features.Base))
}