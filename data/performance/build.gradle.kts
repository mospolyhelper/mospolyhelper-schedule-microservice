plugins {
    id("data-base")
}

dependencies {
    api(projects.data.base)
    api(projects.domain.services)
    implementation(projects.domain.services)
}
