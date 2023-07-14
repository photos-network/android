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

android {
    namespace = "photos.network.common"

    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        // API 23 | android.security.keystore
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
}

kover {
    filters {
        classes {
            excludes += "photos.network.common.Module*"
            excludes += "photos.network.common.BuildConfig"
        }
    }
}

configurations {
    create("testArtifacts"){
        extendsFrom(configurations.testApi.get())
    }
    create("androidTestArtifacts"){
        extendsFrom(configurations.androidTestApi.get())
    }
}

dependencies {
    api(libs.androidx.core.core.ktx)
    testApi(libs.androidx.test.core.ktx)

    // Coroutines
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    testApi(libs.kotlinx.coroutines.test)

    // Coroutine Lifecycle Scopes
    api(libs.lifecycle.runtime.ktx)
    api(libs.lifecycle.viewmodel.ktx)

    // Koin dependency injection
    api(libs.bundles.koin)
    testApi(libs.koin.test)

    // logging
    api(libs.logcat)

    // serialization
    api(libs.kotlin.serialization)
    api(libs.kotlinx.serialization.json)

    // workmanager
    api(libs.work.runtime.ktx)
    androidTestApi(libs.work.testing)

    // httpclient
    api(libs.bundles.ktor)
    testApi(libs.ktor.client.mock.jvm)

    // exifinterface
    api(libs.exifinterface)

    // security crypto
    api(libs.security.crypto)

    // testing
    testApi(libs.mockk)
    testApi(libs.truth)
    testApi(libs.junit.junit)

    // instrumented tests
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.truth)
}
