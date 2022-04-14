plugins {
    id("feature-base")
}

dependencies {
    api(project(":features:base"))
    api(project(":data:peoples"))
    api(project(":domain:peoples"))
}