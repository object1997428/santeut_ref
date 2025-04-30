plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

//    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.santeut"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.santeut"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
//    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.database.ktx)

//    implementation(libs.androidx.material3.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")

    // navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
//    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging-ktx")

    // 권한 요청 관련
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // ui
    implementation("androidx.compose.ui:ui:1.6.6")
    implementation("androidx.compose.material:material:1.6.6")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.6")
    implementation("com.google.accompanist:accompanist-pager:0.20.1")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.20.1")
    implementation("com.google.accompanist:accompanist-flowlayout:0.26.3-beta")

    // icons
    implementation("androidx.compose.material:material-icons-extended:1.6.6")

    // LiveData
    implementation("androidx.compose.runtime:runtime-livedata:1.2.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Camera
    implementation("androidx.camera:camera-camera2:1.4.0-alpha02")
    implementation("androidx.camera:camera-lifecycle:1.4.0-alpha02")
    implementation("androidx.camera:camera-view:1.4.0-alpha02")
    implementation("io.github.ujizin:camposer:0.4.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.3.1")

    // Translate
    implementation("com.deepl.api:deepl-java:1.5.0")

    // Accompanist Pager
    implementation ("com.google.accompanist:accompanist-pager:<version>")

    // Wear
    implementation("com.google.android.gms:play-services-wearable:18.1.0")

    // navermap
    implementation ("io.github.fornewid:naver-map-compose:1.7.0")
    implementation ("io.github.fornewid:naver-map-location:21.0.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation ("org.locationtech.jts:jts-core:1.18.2")

    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("org.jsoup:jsoup:1.15.3")

    implementation("androidx.compose.material3:material3:1.0.0")
    implementation("com.google.accompanist:accompanist-pager:0.26.5-rc")
}