plugins {
    id("data-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.data.base)
}
