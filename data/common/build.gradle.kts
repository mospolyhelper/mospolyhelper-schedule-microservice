plugins {
    id("data-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(project(Modules.Data.Base))
}