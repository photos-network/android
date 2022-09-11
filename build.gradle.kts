buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(Android.tools.build.gradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
        classpath("com.malinskiy.marathon:marathon-gradle-plugin:_")
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
}
