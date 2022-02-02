rootProject.name = "PhotosNetwork"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.namespace) {
                "com.android" -> useModule("com.android.tools.build:gradle:${requested.version}")
            }
            when (requested.id.id) {
                "marathon" -> useModule("com.malinskiy.marathon:marathon-gradle-plugin:${requested.version}")
            }
        }
    }
}

include(":app")
include(":domain")
include(":data")
