plugins {
    id("feature-base")
}

dependencies {
    api(project(":features:base"))
    api(project(":data:payments"))
    api(project(":domain:payments"))
}