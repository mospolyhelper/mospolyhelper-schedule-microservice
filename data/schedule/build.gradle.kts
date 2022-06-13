plugins {
    id("data-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.data.base)
    api(projects.data.common)
    api(projects.domain.schedule)
    api(projects.domain.peoples)
    api(projects.data.peoples)
}