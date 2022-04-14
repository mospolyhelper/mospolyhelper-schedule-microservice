plugins {
    id("feature-base")
}

dependencies {
    api(project(":domain:auth"))
    api(project(":domain:personal"))
    api(project(":features:base"))
}