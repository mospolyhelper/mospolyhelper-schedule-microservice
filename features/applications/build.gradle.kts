plugins {
    id("feature-base")
}

dependencies {
    api(project(":features:base"))
    api(project(":data:applications"))
    api(project(":domain:applications"))
}