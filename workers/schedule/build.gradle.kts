plugins {
    id("worker-base")
}

version = "0.0.1"

dependencies {
    implementation(projects.data.base)
    implementation(projects.data.schedule)
    implementation(projects.domain.base)
    implementation(projects.domain.schedule)
    implementation(projects.features.schedule)
}
