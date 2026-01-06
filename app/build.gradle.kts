plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.scainterclients"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.scainterclients"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    // Define the Compose BOM once. This manages versions for all other Compose libraries.
    implementation(platform(libs.androidx.compose.bom))

    // Material Design 3 and extended icons
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Core Android KTX and Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI elements
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.foundation.layout) // Use the alias from libs.versions.toml

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Test implementations
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug implementations
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // QR Code
    implementation(libs.core)

    // Retrofit, Gson, and OkHttp (using the v2.9.0/v4.12.0 versions you defined)
    implementation(libs.retrofit.v290)
    implementation(libs.converter.gson.v290)
    implementation(libs.okhttp.v4120)
    implementation(libs.logging.interceptor.v4120) // Use the specific version alias

    // Coroutines (using the v1.8.0 version)
    implementation(libs.kotlinx.coroutines.android.v180)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // ViewModel & Lifecycle components for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // This is the correct dependency for collectAsStateWithLifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)
}

