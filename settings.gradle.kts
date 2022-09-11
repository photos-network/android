import de.fayard.refreshVersions.core.StabilityLevel
rootProject.name = "PhotosNetwork"

plugins {
    id("de.fayard.refreshVersions") version "0.40.2"
}

refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel != StabilityLevel.Stable
    }
}

include(":app")
include(":domain")
include(":data")
