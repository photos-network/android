plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    alias(libs.plugins.spotless)
}

spotless {
    kotlin {
        target("src/*/kotlin/**/*.kt")
        ktlint( libs.versions.ktlint.get())
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
}

detekt {
    config = files("$rootDir/detekt.yml")
}

android {
    namespace = "photos.network.ui.photos"

    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        // API 26 | required by: Java 8 Time API
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.material.ExperimentalMaterialApi"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(projects.common)
    testImplementation(project(":common", "testArtifacts"))
    androidTestImplementation(project(":common", "androidTestArtifacts"))

    api(projects.domain.photos)
    implementation(projects.ui.common)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)

    // accompanist
    implementation(libs.bundles.accompanist)

    testImplementation(libs.mockk)
    testImplementation(libs.junit.junit)
    testImplementation(libs.truth)
    testImplementation(libs.core.testing)
}
