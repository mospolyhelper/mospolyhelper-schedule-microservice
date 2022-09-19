plugins {
    id("feature-base")
}

dependencies {
    api(projects.domain.auth)
    api(projects.domain.personal)
    api(projects.features.base)
}
