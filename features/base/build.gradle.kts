plugins {
    id("feature-base")
}

dependencies {
    api(project(Modules.Data.Base))
    api(project(Modules.Domain.Base))
}