plugins {
    id("data-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(project(Modules.Data.Base))
    api(project(Modules.Domain.Schedule))
}