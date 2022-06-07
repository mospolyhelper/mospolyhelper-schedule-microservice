plugins {
    id("domain-base")
}

dependencies {
    api(project(Modules.Domain.Base))
    implementation(project(Modules.Domain.Peoples))
}