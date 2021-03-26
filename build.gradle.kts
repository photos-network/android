import java.net.URI

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha11")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.31.2-alpha")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url = URI("https://kotlin.bintray.com/kotlinx")
        }
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
