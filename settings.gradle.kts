enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
        }
    }

    plugins {
        kotlin("android").version("1.8.10")
        id("com.android.application").version("7.4.0")
        id("com.android.library").version("7.4.0")
        // kotlin("kapt").version("1.8.10")
        id("com.google.devtools.ksp").version("1.8.10-1.0.9")
    }
}

rootProject.name = "PhotosNetwork"

include(":app")

include(":ui:albums")
include(":ui:folders")
include(":ui:photos")
include(":ui:settings")
include(":ui:sharing")

include(":ui:common")

include(":domain:albums")
include(":domain:folders")
include(":domain:photos")
include(":domain:settings")
include(":domain:sharing")

include(":repository:photos")
include(":repository:settings")
include(":repository:sharing")

// Persist albums & photos enriched with user info or from backend
include(":database:albums")
include(":database:photos")
include(":database:settings")
include(":database:sharing")

// communication via REST API with core instance
include(":api")

// instance and account
include(":system:account")

// folders via Android Filesystem
include(":system:filesystem")

// media items via Android Media Store
include(":system:mediastore")

// shared code
include(":common")
