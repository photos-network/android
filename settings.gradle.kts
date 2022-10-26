import de.fayard.refreshVersions.core.StabilityLevel
rootProject.name = "PhotosNetwork"

plugins {
    id("de.fayard.refreshVersions") version "0.51.0"
}

refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel != StabilityLevel.Stable
    }
}

include(":app")
include(":domain")
include(":data")
