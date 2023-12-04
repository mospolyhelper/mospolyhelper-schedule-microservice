plugins {
    id("feature-base")
}

dependencies {
    api(projects.domain.auth)
    api(projects.domain.services)
    api(projects.features.base)
}
