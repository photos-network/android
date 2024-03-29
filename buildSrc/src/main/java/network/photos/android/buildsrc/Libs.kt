@file:Suppress("unused")

package network.photos.android.buildsrc

object Libs {
    object Kotlin {
        private const val version = "1.4.32"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.4.3"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Google {
        private const val version = "2.35.1"
        const val material = "com.google.android.material:material:1.3.0"
        const val hiltAndroidPlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:$version"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val junit = "com.google.dagger:hilt-android-compiler:$version"
        const val gson = "com.google.code.gson:gson:2.8.6"
        const val navigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha02"

        object Accompanist {
            private const val version = "0.10.0"
            const val coil = "com.google.accompanist:accompanist-coil:$version"
            const val glide = "com.google.accompanist:accompanist-glide:$version"
            const val insets = "com.google.accompanist:accompanist-insets:$version"
            const val systemuicontroller = "com.google.accompanist:accompanist-systemuicontroller:$version"
            const val appcompat = "com.google.accompanist:accompanist-appcompat-theme:$version"
            const val pager = "com.google.accompanist:accompanist-pager:$version"
            const val flowlayout = "com.google.accompanist:accompanist-flowlayout:$version"
            const val swiperefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
        }
    }

    object Squareup {
        object Retrofit {
            private const val version = "2.9.0"
            const val retrofit = "com.squareup.retrofit2:retrofit:$version"
            const val converterGson = "com.squareup.retrofit2:converter-gson:$version"
            const val converterMoshi = "com.squareup.retrofit2:converter-moshi:$version"
        }

        object Moshi {
            private const val version = "1.8.0"
            const val moshi = "com.squareup.moshi:moshi:$version"
            const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
        }

        object OkHttp {
            private const val version = "4.9.0"
            const val okhttp = "com.squareup.okhttp3:okhttp:$version"
            const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
        }
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0-rc01"
        const val coreKtx = "androidx.core:core-ktx:1.5.0-alpha04"

        object Compose {
            // member is also used to set the compiler extension version
            @Suppress("MemberVisibilityCanBePrivate")
            const val version = "1.0.0-beta07"

            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:$version"
            const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"
        }

        object Security {
            private const val version = "1.0.0"
            const val securityCrypto = "androidx.security:security-crypto:$version"
        }

        object Navigation {
            private const val version = "2.4.0-alpha01"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val compose = "androidx.navigation:navigation-compose:$version"
        }

        object Test {
            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.2-rc01"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
        }

        object Lifecycle {
            private const val version = "2.3.1"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val hiltLifeCycle = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha02"
            const val navigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha02"
        }
    }

    object UI {
        object Glide {
            const val glide = "com.github.bumptech.glide:glide:4.11.0"
        }
    }

    object Test {
        object Robolectric {
            private const val version = "4.5.1"
            const val robolectric = "org.robolectric:robolectric:$version"
        }
    }
}
