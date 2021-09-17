import java.net.URI

buildscript {
    val kotlinVersion = "1.4.32"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0-alpha12")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.36")
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
