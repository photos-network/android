import network.photos.android.buildsrc.Libs

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = 30
    defaultConfig {
        minSdk = 23
        targetSdk = 30

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
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }

    packagingOptions {
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
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

    // security
    api(Libs.AndroidX.Security.securityCrypto)

    // testing
    androidTestImplementation(Libs.AndroidX.Test.core)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
    androidTestImplementation(Libs.AndroidX.Test.rules)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Compose.test)
    androidTestImplementation(Libs.AndroidX.Compose.uiTest)

    // http client
    api(Libs.Squareup.OkHttp.okhttp)
    api(Libs.Squareup.OkHttp.logging)

    // rest client
    api(Libs.Squareup.Retrofit.retrofit)
    api(Libs.Squareup.Retrofit.converterGson)
    api(Libs.Squareup.Retrofit.converterMoshi)

    // json parsing
    api(Libs.Squareup.Moshi.moshi)
    kapt(Libs.Squareup.Moshi.codegen)
}
