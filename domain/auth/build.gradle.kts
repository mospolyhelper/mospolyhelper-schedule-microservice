plugins {
    id("domain-base")
}

dependencies {
    api(projects.domain.base)
    implementation(libs.jwt)
}
