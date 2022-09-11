plugins {
    id("com.android.library")
    id("com.diffplug.spotless")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("jacoco")
}

spotless {
    kotlin {
        target("src/*/kotlin/**/*.kt")
        ktlint("0.43.2")
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
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

    testCoverage {
        // needed to force the jacoco version
        jacocoVersion = "0.8.7"
        version = "0.8.7"
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
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

repositories {
    google()
    mavenCentral()
}

dependencies {
    api(AndroidX.core.ktx)

    // Coroutines
    api(KotlinX.coroutines.core)
    api(KotlinX.coroutines.android)

    // Coroutine Lifecycle Scopes
    api(AndroidX.lifecycle.runtimeKtx)
    api(AndroidX.lifecycle.viewModelKtx)

    // Koin dependency injection
    api(Koin.core)
    testApi(Koin.test)
    api(Koin.android)
    api(Koin.workManager)
    api("io.insert-koin:koin-androidx-navigation:_")
    api(Koin.compose)

    // Persistence
    api(AndroidX.room.runtime)
    api(AndroidX.room.ktx)
    kapt(AndroidX.room.compiler)
    androidTestImplementation(AndroidX.room.testing)

    // workmanager
    api(AndroidX.work.runtimeKtx)
    androidTestApi(AndroidX.work.testing)

    // exifinterface
    api(AndroidX.exifInterface)

    // httpclient
    implementation(Ktor.client.core)
    implementation(Ktor.client.okHttp)
    implementation(Ktor.client.cio)
    implementation(Ktor.client.auth)
    implementation(Ktor.client.serialization)
    implementation("io.ktor:ktor-client-content-negotiation:_")
    implementation("io.ktor:ktor-serialization-kotlinx-json:_")
    implementation("io.ktor:ktor-client-logging-jvm:_")
    implementation("io.ktor:ktor-client-mock-jvm:_")

    // logging
    api(Square.logcat)

    // serialization
    api(KotlinX.serialization.json)
    api(AndroidX.security.crypto)

    // testing
    testApi(AndroidX.test.ext.junitKtx)
    testApi(Testing.junit4)
    testApi("com.google.truth:truth:_")
    testApi(Testing.mockK)
    testApi(KotlinX.coroutines.test)
    testApi(AndroidX.archCore.testing)

    androidTestApi(AndroidX.test.core)
    androidTestApi(AndroidX.test.coreKtx)
    androidTestApi(AndroidX.test.ext.junit)
    androidTestApi(AndroidX.test.ext.junitKtx)
    androidTestApi(AndroidX.test.ext.truth)
    androidTestApi(AndroidX.test.monitor)
    androidTestApi(AndroidX.test.orchestrator)
    androidTestApi(AndroidX.test.runner)
    androidTestApi(AndroidX.test.rules)
    androidTestApi(AndroidX.test.services)
    androidTestApi(Testing.mockK)
}
