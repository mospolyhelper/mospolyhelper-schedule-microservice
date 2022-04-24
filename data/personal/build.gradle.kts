plugins {
    id("data-base")
}

dependencies {
    api(project(Modules.Data.Base))
    api(project(Modules.Domain.Personal))
}