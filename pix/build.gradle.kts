plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("kotlin-parcelize")
//    id("dagger.hilt.android.plugin")
//    id("maven-publish")
//    id("com.vanniktech.maven.publish")
}

android {
    namespace = "io.ak1.pix"
    compileSdk = 34
//    buildToolsVersion "31.0.0"

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
//        release {
//            minifyEnabled = false
//            proguardFiles(getDefaultProguardFile(("proguard-android-optimize.txt"), ("proguard-rules.pro")))
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = ("11")
    }

    buildFeatures {
        viewBinding = true
    }
}

//repositories {
//    mavenCentral()
////    google()
//}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation ("com.google.android.material:material:1.3.0")
    implementation ("androidx.fragment:fragment-ktx:1.3.4")

    // CameraX core library using camera2 implementation
    implementation ("androidx.camera:camera-camera2:1.2.3")
    // CameraX Lifecycle Library
    implementation ("androidx.camera:camera-lifecycle:1.2.3")
    // CameraX View class
    implementation ("androidx.camera:camera-view:1.2.3")
    // If you want to additionally use the CameraX Extensions library
    implementation ("androidx.camera:camera-extensions:1.2.3")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.13.2")
    implementation("com.github.bumptech.glide:recyclerview-integration:4.12.0")
        // Excludes the support library because it's already included by Glide.
    //Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0-native-mt")

    androidTestImplementation ("androidx.test.ext:junit:1.1.4")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
}