import com.github.triplet.gradle.androidpublisher.ResolutionStrategy
import com.github.triplet.gradle.androidpublisher.ReleaseStatus

plugins {
    id("com.android.application") version "7.0.4"
    id("com.diffplug.spotless") version "6.0.4"
    kotlin("android") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.serialization") version "1.6.0"
    id("marathon") version "0.6.4"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("com.github.triplet.play") version "3.7.0"
    id("jacoco")
}

spotless {
    kotlin {
        target("src/*/kotlin/**/*.kt")
        ktlint("0.43.2")
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

jacoco {
    toolVersion = "0.8.7"
}

project.afterEvaluate {
    tasks.create<JacocoReport>(name = "testCoverage") {
        dependsOn("testDebugUnitTest")
        group = "Reporting"
        description = "Generate jacoco coverage reports"

        reports {
            html.required.set(true)
            xml.required.set(true)
            csv.required.set(true)
        }

        val excludes = listOf<String>(
            // android
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*",
            // kotlin
            "**/*MapperImpl*.*",
            "**/*\$ViewInjector*.*",
            "**/*\$ViewBinder*.*",
            "**/BuildConfig.*",
            "**/*Component*.*",
            "**/*BR*.*",
            "**/Manifest*.*",
            "**/*\$Lambda$*.*",
            "**/*Companion*.*",
            "**/*Module*.*",
            "**/*Dagger*.*",
            "**/*Hilt*.*",
            "**/*MembersInjector*.*",
            "**/*_MembersInjector.class",
            "**/*_Factory*.*",
            "**/*_Provide*Factory*.*",
            "**/*Extensions*.*",
            // sealed and data classes
            "**/*$Result.*",
            "**/*$Result$*.*"
        )

        val kotlinClasses = fileTree(baseDir = "$buildDir/tmp/kotlin-classes/debug") {
            exclude(excludes)
        }

        classDirectories.setFrom(kotlinClasses)

        val androidTestData = fileTree(baseDir = "$buildDir/outputs/code_coverage/debugAndroidTest/connected/")

        executionData(files(
            "${project.buildDir}/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            androidTestData
        ))
    }
}

// https://detekt.dev/gradle.html
detekt {
    config = files("../detekt.yml")
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
        versionCode = 5
        versionName = "0.1.0"

        testInstrumentationRunner = "photos.network.PhotosNetworkJUnitRunner"
    }

    testCoverage {
        // needed to force the jacoco version
        jacocoVersion = "0.8.7"
        version = "0.8.7"
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
            isTestCoverageEnabled = true
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
        kotlinCompilerExtensionVersion = "1.1.0-alpha04"
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
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
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

repositories {
    google()
    mavenCentral()
}

dependencies {
    api(project(":domain"))
    testImplementation(project(":data", "testArtifacts"))
    androidTestImplementation(project(":data", "androidTestArtifacts"))

    // Compose
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.1")
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.material3:material3:1.0.0-alpha06")
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    implementation("androidx.navigation:navigation-compose:2.4.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha14")
    implementation("androidx.paging:paging-common-ktx:3.1.0")
    androidTestApi("androidx.compose.ui:ui-test:1.1.1")
    androidTestApi("androidx.compose.ui:ui-test-junit4:1.1.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.1")
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
