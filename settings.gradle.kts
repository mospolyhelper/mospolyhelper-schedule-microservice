rootProject.name = "mospolyhelper-schedule"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include(":app")
include(":data")
include(":test")
include(":data:base")
include(":data:schedule")