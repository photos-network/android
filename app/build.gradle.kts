import com.github.triplet.gradle.androidpublisher.ResolutionStrategy
import com.github.triplet.gradle.androidpublisher.ReleaseStatus

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    alias(libs.plugins.spotless)
    alias(libs.plugins.grgit)
    alias(libs.plugins.triplet)
}

spotless {
    kotlin {
        target("src/*/kotlin/**/*.kt")
        ktlint( libs.versions.ktlint.get())
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
}

// key.properties
val releaseKeystore: String by project
val releaseStorePassword: String by project
val releaseKeyAlias: String by project
val releaseKeyPassword: String by project

// deployment
play {
    // publishing will be enabled for release flavor only
    enabled.set(false)

    serviceAccountCredentials.set(rootProject.file("gradle_playstore_publisher_credentials.json"))
    defaultToAppBundles.set(true)

    promoteTrack.set("alpha")
    resolutionStrategy.set(ResolutionStrategy.AUTO)
    releaseStatus.set(ReleaseStatus.COMPLETED)
}

// https://detekt.dev/gradle.html
detekt {
    config = files("../detekt.yml")
}

android {
    namespace = "photos.network"

    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "photos.network"
        // API 21 | required by: security-crypto library
        // API 23 | required by: security.crypto.MasterKey
        // API 24 | required by: networkSecurityConfig
        // API 26 | required by: Java 8 Time API
        minSdk = 26
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = grgit.log().size
        versionName = "0.1.0-${grgit.head().abbreviatedId}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        named("debug") {
            storeFile = file("../android_debug.keystore")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
        create("release") {
            storeFile = file(path = releaseKeystore)
            storePassword = releaseStorePassword
            keyAlias = releaseKeyAlias
            keyPassword = releaseKeyPassword
        }
    }

    (this as ExtensionAware).extensions.configure<
            NamedDomainObjectContainer<com.github.triplet.gradle.play.PlayPublisherExtension>>("playConfigs") {
        register("release") {
            enabled.set(true)
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isMinifyEnabled = !gradle.startParameter.taskNames.any { it.contains("AndroidTest") }
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro",
                "proguard-rules-debug.pro",
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        compose = true
        viewBinding = false
        buildConfig = true
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-Xallow-unstable-dependencies"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=coil.annotation.ExperimentalCoilApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.Experimental"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.animation.ExperimentalAnimationApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.material.ExperimentalMaterialApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=com.google.accompanist.pager.ExperimentalPagerApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true

            // Disable kover for non-debug builds
            all {
                it.extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
                    isDisabled.set(!it.name.contains("testDebug"))
                }
            }
        }
    }

    packagingOptions {
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
        resources.excludes += "META-INF/licenses/ASM"
        resources.pickFirsts.add("win32-x86-64/attach_hotspot_windows.dll")
        resources.pickFirsts.add("win32-x86/attach_hotspot_windows.dll")
    }
}

dependencies {
    implementation(projects.common)
    testImplementation(project(":common", "testArtifacts"))
    androidTestImplementation(project(":common", "androidTestArtifacts"))

    implementation(projects.ui.albums)
    implementation(projects.ui.folders)
    implementation(projects.ui.photos)
    implementation(projects.ui.settings)
    implementation(projects.ui.search)
    implementation(projects.ui.sharing)

    implementation(projects.ui.common)

    // Compose Activity
    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)

    implementation(libs.bundles.accompanist)
    implementation(libs.androidx.window)
//    implementation(libs.androidx.window.core)

    // leakCanary
    debugImplementation(libs.leakcanary.android)

    testImplementation(libs.core.testing)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
