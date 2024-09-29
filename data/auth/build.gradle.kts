plugins {
    id("data-base")
}

dependencies {
    api(projects.data.base)
    api(projects.domain.auth)
    implementation(libs.jwt)
}
