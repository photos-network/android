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
    namespace = "photos.network.repository.photos"

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
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
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

    // workmanager
    api(libs.work.runtime.ktx)
    androidTestApi(libs.work.testing)

    implementation(projects.network)
    api(projects.database.photos)

//    api(AndroidX.core.ktx)

    // Coroutines
//    api(KotlinX.coroutines.core)
//    api(KotlinX.coroutines.android)

    // Coroutine Lifecycle Scopes
//    api(AndroidX.lifecycle.runtime.ktx)
//    api(AndroidX.lifecycle.viewModelKtx)

    // Koin dependency injection
//    api(Koin.core)
//    testApi(Koin.test)
//    api(Koin.android)
//    api(Koin.workManager)
//    api(Koin.navigation)
//    api(Koin.compose)

    // Persistence
//    api(AndroidX.room.runtime)
//    api(AndroidX.room.ktx)
//    androidTestImplementation(AndroidX.room.testing)

    // exifinterface
//    api(AndroidX.exifInterface)

    // httpclient
//    implementation(Ktor.client.core)
//    implementation(Ktor.client.cio)
//    implementation(Ktor.client.cio)
//    implementation(Ktor.client.auth)
//    implementation(Ktor.client.serialization)
//    implementation(Ktor.client.contentNegotiation)
//    implementation(Ktor.plugins.serialization.kotlinx.json)
//    implementation(libs.ktor.client.logging.jvm)
//    implementation(libs.ktor.client.mock.jvm)

    // logging
//    api(Square.logcat)

    // serialization
//    api(KotlinX.serialization.json)
//    api(AndroidX.security.crypto)

    // testing
//    testApi(AndroidX.test.ext.junit.ktx)
//    testApi(Testing.junit4)
//    testApi(libs.com.google.truth.truth)
//    testApi(Testing.mockK)
//    testApi(KotlinX.coroutines.test)
//    testApi(AndroidX.archCore.testing)

//    androidTestApi(AndroidX.test.core)
//    androidTestApi(AndroidX.test.coreKtx)
//    androidTestApi(AndroidX.test.ext.junit)
//    androidTestApi(AndroidX.test.ext.junit.ktx)
//    androidTestApi(AndroidX.test.ext.truth)
//    androidTestApi(AndroidX.test.monitor)
//    androidTestApi(AndroidX.test.orchestrator)
//    androidTestApi(AndroidX.test.runner)
//    androidTestApi(AndroidX.test.rules)
//    androidTestApi(AndroidX.test.services)
//    androidTestApi(Testing.mockK)
}
