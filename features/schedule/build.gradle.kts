plugins {
    id("feature-base")
}

dependencies {
    api(project(":features:base"))
    api(project(":data:schedule"))
    api(project(":domain:schedule"))
    implementation(project(":domain:personal"))
}