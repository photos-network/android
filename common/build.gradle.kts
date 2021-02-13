import network.photos.android.buildsrc.Libs

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }

    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-alpha11"
    }

    packagingOptions {
        exclude("META-INF/licenses/**")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Coroutines.android)

    api(Libs.AndroidX.Compose.navigation)
    api(Libs.AndroidX.Compose.foundation)
    api(Libs.AndroidX.Compose.layout)
    api(Libs.AndroidX.Compose.material)
    api(Libs.AndroidX.Compose.materialIconsExtended)
    api(Libs.AndroidX.Compose.tooling)
    api(Libs.AndroidX.Compose.uiUtil)
    api(Libs.AndroidX.Compose.runtime)
    api(Libs.AndroidX.Compose.runtimeLivedata)
    api(Libs.AndroidX.Compose.viewBinding)
    androidTestImplementation(Libs.AndroidX.Compose.test)
    androidTestImplementation(Libs.AndroidX.Compose.uiTest)

    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xallow-jvm-ir-dependencies")
    }
}
