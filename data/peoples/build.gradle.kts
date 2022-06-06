plugins {
    id("data-base")
}

dependencies {
    api(project(Modules.Data.Base))
    api(project(Modules.Data.Personal))
    api(project(Modules.Domain.Peoples))
}