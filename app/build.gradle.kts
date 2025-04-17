// ADDED THIS SECTION FOR API KEY STORAGE
import java.util.Properties
// Create an instance of Properties to hold our loaded key-value pairs
val localProperties = Properties()
// Get a reference to the local.properties file in the root of the project
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    // Load the contents of the local.properties file into the Properties object
    localProperties.load(localPropertiesFile.inputStream())
}


plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.firebase-perf")
//    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ingrediscan"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ingrediscan"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // USED FOR API KEY STORAGE
        // Inject my API key as a build config field
        buildConfigField(
            "String",
            "USDA_API_KEY",
            "\"${localProperties["USDA_API_KEY"]}\""
        )

        buildFeatures {
            buildConfig = true
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
        viewBinding = true
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
    implementation(libs.vision.common)
    implementation(libs.kotlinx.coroutines.play.services)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dependencies for OCR and Barcode
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation (libs.barcode.scanning)
    implementation (libs.text.recognition)


    // Dependencies for ApiCalls.kt
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Dependencies for FoodApiServiceTest.kt
    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation ("androidx.test:core:1.5.0")
    // Coroutines Test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation ("org.robolectric:robolectric:4.10")
    // Retrofit + MockWebServer for API testing
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    // Truth library for better assertions
    testImplementation("com.google.truth:truth:1.1.3")

    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-perf")

    // Compose dependencies to trasnfer jetpack compose to XML
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.8")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    
        //CamaraX Dependencies
    val cameraVersions = "1.4.1"
    implementation("androidx.camera:camera-core:${cameraVersions}")
    implementation("androidx.camera:camera-camera2:${cameraVersions}")
    implementation("androidx.camera:camera-lifecycle:${cameraVersions}")
    implementation("androidx.camera:camera-view:${cameraVersions}")
    implementation("androidx.camera:camera-extensions:${cameraVersions}")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation ("androidx.appcompat:appcompat:1.7.0")

    // Search page dependencies
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

}