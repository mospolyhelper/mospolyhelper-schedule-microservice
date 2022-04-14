plugins {
    id("feature-base")
}

dependencies {
    api(project(":features:base"))
    api(project(":data:performance"))
    api(project(":domain:performance"))
}