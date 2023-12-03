plugins {
    id("data-base")
}

dependencies {
    api(projects.data.base)
    api(projects.domain.performance)
    implementation(projects.domain.personal)
}
