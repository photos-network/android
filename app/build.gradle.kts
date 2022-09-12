import com.github.triplet.gradle.androidpublisher.ResolutionStrategy
import com.github.triplet.gradle.androidpublisher.ReleaseStatus

plugins {
    id("com.android.application")
    id("com.diffplug.spotless")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("io.gitlab.arturbosch.detekt")
    id("com.github.triplet.play")
    id("org.ajoberstar.grgit")
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

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "photos.network"
        // API 21 | required by: security-crypto library
        // API 23 | required by: security.crypto.MasterKey
        // API 24 | required by: networkSecurityConfig
        // API 26 | required by: Java 8 Time API
        minSdk = 26
        targetSdk = 31
        versionCode = grgit.log().size
        versionName = "0.1.0-${grgit.head().abbreviatedId}"

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
        kotlinCompilerExtensionVersion = "1.2.0"
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
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.runtime.liveData)
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.navigation.compose)
    implementation(AndroidX.constraintLayout.compose)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.paging.compose)
    implementation(AndroidX.paging.commonKtx)
    androidTestApi(AndroidX.compose.ui.test)
    androidTestApi(AndroidX.compose.ui.testJunit4)
    debugImplementation(AndroidX.compose.ui.testManifest)
    debugApi(AndroidX.compose.ui.tooling)

    // accompanist
    implementation("com.google.accompanist:accompanist-navigation-animation:_")
    implementation(Google.accompanist.systemuicontroller)
    implementation("com.google.accompanist:accompanist-placeholder:_")
    implementation(Google.accompanist.flowlayout)
    implementation(Google.accompanist.insets)
    implementation(Google.accompanist.pager)
    implementation(Google.accompanist.swiperefresh)
    implementation("com.google.accompanist:accompanist-permissions:_")

    // design
    implementation(Google.android.material)

    // Coil
    implementation(COIL)
    implementation(COIL.compose)

    // retrofit
    implementation(Square.retrofit2)
    implementation(Square.okHttp3.loggingInterceptor)

    // serialization
    implementation(KotlinX.serialization.json)
    implementation(JakeWharton.retrofit2.converter.kotlinxSerialization)

    // leakCanary
    debugImplementation(Square.leakCanary.android)
}
