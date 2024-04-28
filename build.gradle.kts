// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        /*classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")*/
        classpath("com.google.gms:google-services:4.4.1")
    }
}


plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.3.0" apply false
}