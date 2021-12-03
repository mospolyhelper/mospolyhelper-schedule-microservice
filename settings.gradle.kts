rootProject.name = "mospolyhelper-web"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include(":microservices")
include(":microservices:auth")
include(":microservices:schedule")

include(":data")
include(":data:base")
include(":data:schedule")

include(":domain")
include(":domain:base")
include(":domain:schedule")

include(":features")
include(":features:schedule")