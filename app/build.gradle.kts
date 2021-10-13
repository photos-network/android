plugins {
    id("com.android.application") version "7.0.3"
    kotlin("android") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
    id("dagger.hilt.android.plugin") version "2.38.1"
}

repositories {
    google()
    mavenCentral()
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "photos.network"
        // API 21 | required by: security-crypto library
        // API 23 | required by: security.crypto.MasterKey
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        viewBinding = false
        buildConfig = false
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
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-alpha04"
    }

    packagingOptions {
        resources.excludes += "/META-INF/AL2.0"
        resources.excludes += "/META-INF/LGPL2.1"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.activity:activity-compose:1.3.1")

    // Compose
    implementation("androidx.compose.runtime:runtime-livedata:1.1.0-alpha05")
    implementation("androidx.compose.ui:ui:1.0.3")
    implementation("androidx.compose.material:material:1.0.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.3")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha10")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc01")
    implementation("androidx.compose.material:material-icons-extended:1.0.3")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    // Hilt dependency injection
    implementation("com.google.dagger:hilt-android:2.38.1")
    kapt("com.google.dagger:hilt-android-compiler:2.38.1")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha10")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")

    // Persistence
    implementation("androidx.room:room-runtime:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")

    // accompanist
    implementation("com.google.accompanist:accompanist-navigation-animation:0.19.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.19.0")
    implementation("com.google.accompanist:accompanist-placeholder:0.19.0")
    implementation("com.google.accompanist:accompanist-flowlayout:0.19.0")
    implementation("com.google.accompanist:accompanist-insets:0.19.0")
    implementation("com.google.accompanist:accompanist-pager:0.19.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.19.0")

    // design
    implementation("com.google.android.material:material:1.4.0")

    // Coil
    implementation("io.coil-kt:coil:1.3.2")
    implementation("io.coil-kt:coil-compose:1.3.2")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    implementation("androidx.security:security-crypto:1.1.0-alpha03")
    implementation("com.google.code.gson:gson:2.8.6")

    // testing
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.0.3")


    // testing
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.4-alpha03")
    androidTestImplementation("androidx.compose.ui:ui-test:1.0.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.3")
}
