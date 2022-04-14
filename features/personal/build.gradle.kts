plugins {
    id("feature-base")
}

dependencies {
    api(project(":features:base"))
    api(project(":data:personal"))
    api(project(":domain:personal"))
}