buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.15") // Replace with actual version if not using libs
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.2.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}