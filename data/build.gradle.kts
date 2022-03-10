plugins {
    id("com.android.library") version "7.0.4"
    id("com.diffplug.spotless") version "6.0.4"
    kotlin("android") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
}

repositories {
    google()
    mavenCentral()
}

spotless {
    kotlin {
        target("src/*/java/**/*.kt")
        ktlint("0.43.2")
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 26

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

    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
    packagingOptions {
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
        resources.excludes += "META-INF/licenses/ASM"
        resources.pickFirsts.add("win32-x86-64/attach_hotspot_windows.dll")
        resources.pickFirsts.add("win32-x86/attach_hotspot_windows.dll")
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
    api("androidx.core:core-ktx:1.7.0")

    // Coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    // Coroutine Lifecycle Scopes
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")

    // Koin dependency injection
    api("io.insert-koin:koin-core:3.1.4")
    testApi("io.insert-koin:koin-test:3.1.4")
    api("io.insert-koin:koin-android:3.1.4")
    api("io.insert-koin:koin-androidx-workmanager:3.1.4")
    api("io.insert-koin:koin-androidx-navigation:3.1.4")
    api("io.insert-koin:koin-androidx-compose:3.1.4")

    // Persistence
    api("androidx.room:room-runtime:2.4.1")
    api("androidx.room:room-ktx:2.4.1")
    kapt("androidx.room:room-compiler:2.4.1")
    androidTestImplementation("androidx.room:room-testing:2.4.1")

    // workmanager
    api("androidx.work:work-runtime-ktx:2.7.1")
    androidTestApi("androidx.work:work-testing:2.7.1")

    // exifinterface
    api("androidx.exifinterface:exifinterface:1.3.3")

    // httpclient
    api("io.ktor:ktor-client-core:1.6.7")
    api("io.ktor:ktor-client-android:1.6.6")
    api("io.ktor:ktor-client-cio:1.6.7")
    api("io.ktor:ktor-client-auth:1.6.6")
    api("io.ktor:ktor-client-serialization:1.6.6")
    api("io.ktor:ktor-client-logging-jvm:1.6.6")
    androidTestApi("io.ktor:ktor-client-mock-jvm:1.6.6")

    // logging
    api("com.squareup.logcat:logcat:0.1")

    // serialization
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    api("androidx.security:security-crypto:1.1.0-alpha03")

    // testing
    testApi("androidx.test.ext:junit-ktx:1.1.3")
    testApi("junit:junit:4.13.2")
    testApi("com.google.truth:truth:1.1.3")
    testApi("io.mockk:mockk:1.12.3")
    testApi("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    testApi("androidx.arch.core:core-testing:2.1.0")

    androidTestApi("androidx.test:core:1.4.0")
    androidTestApi("androidx.test:core-ktx:1.4.0")
    androidTestApi("androidx.test.ext:junit:1.1.3")
    androidTestApi("androidx.test.ext:junit-ktx:1.1.3")
    androidTestApi("androidx.test.ext:truth:1.4.0")
    androidTestApi("androidx.test:monitor:1.5.0")
    androidTestApi("androidx.test:orchestrator:1.4.1")
    androidTestApi("androidx.test:runner:1.4.0")
    androidTestApi("androidx.test:rules:1.4.0")
    androidTestApi("androidx.test.services:test-services:1.4.1")
}
