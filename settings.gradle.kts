rootProject.name = "mospolyhelper-web"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    val kotlinVersion = "1.7.10"

    plugins {
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":microservices")
include(":microservices:edugma")

include(":workers")
include(":workers:schedule")

include(":data")
include(":data:base")
include(":data:common")
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
