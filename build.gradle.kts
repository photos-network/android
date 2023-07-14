plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    // kotlin("kapt") apply false
    id("com.google.devtools.ksp") apply false
    alias(libs.plugins.kover)
}

koverMerged {
    enable()

    xmlReport {
        onCheck.set(false)
        reportFile.set(layout.buildDirectory.file("$buildDir/reports/kover/result.xml"))
    }
    htmlReport {
        onCheck.set(false)
        reportDir.set(layout.buildDirectory.dir("$buildDir/reports/kover/html-result"))
    }
}
