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
include(":microservices:account")

include(":workers")
include(":workers:schedule")

include(":data")
include(":data:base")
include(":data:auth")
include(":data:schedule")
include(":data:performance")
include(":data:peoples")
include(":data:applications")
include(":data:payments")
include(":data:personal")

include(":domain")
include(":domain:base")
include(":domain:user")
include(":domain:auth")
include(":domain:schedule")
include(":domain:performance")
include(":domain:peoples")
include(":domain:applications")
include(":domain:payments")
include(":domain:personal")

include(":features")
include(":features:auth")
include(":features:base")
include(":features:schedule")
include(":features:performance")
include(":features:peoples")
include(":features:applications")
include(":features:payments")
include(":features:personal")