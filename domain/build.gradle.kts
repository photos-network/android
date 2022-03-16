plugins {
    id("com.android.library") version "7.0.4"
    id("com.diffplug.spotless") version "6.0.4"
    kotlin("android") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
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

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
    packagingOptions {
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    api(project(":data"))
    testImplementation(project(":data", "testArtifacts"))
    androidTestImplementation(project(":data", "androidTestArtifacts"))
}
