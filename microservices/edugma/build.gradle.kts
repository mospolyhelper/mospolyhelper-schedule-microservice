plugins {
    id("microservice-base")
}

version = "0.0.1"

dependencies {
    implementation(projects.data.auth)
    implementation(projects.data.performance)
    implementation(projects.data.peoples)
    implementation(projects.data.applications)
    implementation(projects.data.payments)
    implementation(projects.data.personal)

    implementation(projects.data.common)

    implementation(projects.data.base)
    implementation(projects.data.schedule)

    implementation(projects.domain.base)
    implementation(projects.domain.auth)
    implementation(projects.domain.schedule)


    implementation(projects.features.auth)
    implementation(projects.features.schedule)
    implementation(projects.features.performance)
    implementation(projects.features.peoples)
    implementation(projects.features.applications)
    implementation(projects.features.payments)
    implementation(projects.features.personal)






}
