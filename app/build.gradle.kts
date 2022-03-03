plugins {
    id("com.android.application") version "7.0.4"
    id("com.diffplug.spotless") version "6.0.4"
    kotlin("android") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.serialization") version "1.6.0"
    id("marathon") version "0.6.4"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("com.github.triplet.play") version "3.7.0"
}

// key.properties
val releaseKeystore: String by project
val releaseStorePassword: String by project
val releaseKeyAlias: String by project
val releaseKeyPassword: String by project

repositories {
    google()
    mavenCentral()
}

// https://detekt.dev/gradle.html
detekt {
    config = files("../detekt.yml")
}

spotless {
    kotlin {
        target("src/*/java/**/*.kt")
        ktlint("0.43.2")
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
}

play {
    // credentials
    serviceAccountCredentials.set(rootProject.file("gradle_playstore_publisher_credentials.json"))

    // publish defaults
    defaultToAppBundles.set(true)
    track.set("internal")
    userFraction.set(0.5)
    updatePriority.set(2)
}

marathon {
    applicationPmClear = true
    testApplicationPmClear = true
    shardingStrategy {
        countSharding {
            count = 100
            strictMode = true
        }
    }
    retryStrategy {
        fixedQuota {
            retryPerTestQuota = 3
            totalAllowedRetryQuota = 100
        }
    }
    allureConfiguration {
        enabled = true
    }
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "photos.network"
        // API 21 | required by: security-crypto library
        // API 23 | required by: security.crypto.MasterKey
        // API 24 | required by: networkSecurityConfig
        // API 26 | required by: Java 8 Time API
        minSdk = 26
        targetSdk = 31
        versionCode = 2
        versionName = "0.1.0"

        testInstrumentationRunner = "photos.network.PhotosNetworkJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
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

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xallow-unstable-dependencies"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=coil.annotation.ExperimentalCoilApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.Experimental"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-alpha04"
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
    api(project(":domain"))
    androidTestImplementation(project(":data", "testArtifacts"))

    // Compose
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.1")
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    implementation("androidx.navigation:navigation-compose:2.4.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha14")
    implementation("androidx.paging:paging-common-ktx:3.1.0")
    androidTestApi("androidx.compose.ui:ui-test:1.1.1")
    androidTestApi("androidx.compose.ui:ui-test-junit4:1.1.1")
    debugApi("androidx.compose.ui:ui-tooling:1.1.1")

    // accompanist
    val accompanistVersion = "0.23.1"
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-placeholder:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-flowlayout:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-insets:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-pager:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")

    // design
    implementation("com.google.android.material:material:1.5.0")

    // Coil
    implementation("io.coil-kt:coil:1.3.2")
    implementation("io.coil-kt:coil-compose:1.3.2")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // leakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")
}
