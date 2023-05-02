plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    // kotlin("kapt") apply false
    id("com.google.devtools.ksp") apply false
}
