plugins {
    id("feature-base")
}

dependencies {
    api(projects.features.base)
    api(projects.domain.schedule)
    implementation(projects.domain.personal)
}
