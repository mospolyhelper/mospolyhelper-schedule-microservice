plugins {
    id("data-base")
}

dependencies {
    api(projects.data.base)
    implementation(projects.data.common)
    implementation(projects.data.services)

    implementation(projects.domain.peoples)
    implementation(projects.domain.auth)
}
