plugins {
    id("feature-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.features.base)
    api(projects.domain.schedule)
    implementation(projects.domain.personal)
}
