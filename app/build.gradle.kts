plugins {
    alias(libs.plugins.android.application)
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.videoplayer.videox"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.videoplayer.videox"
        minSdk = 24
        targetSdk = 35
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


    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(project(":mobile-ffmpeg"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:app-update:2.1.0")

    implementation("com.getkeepsafe.taptargetview:taptargetview:1.14.0")

//    implementation("io.reactivex.rxjava3:rxjava:3.1.10")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
//    implementation("io.reactivex:rxjava:1.3.8")

    implementation("org.videolan.android:libvlc-all:3.3.5")

    implementation("org.florescu.android.rangeseekbar:rangeseekbar-library:0.3.0")

    implementation("com.google.android.exoplayer:exoplayer:2.16.0")
    implementation("com.google.android.exoplayer:extension-mediasession:2.16.0")

    //room
    implementation ("androidx.room:room-rxjava2:2.7.1")
//    implementation ("android.arch.persistence.room:runtime:2.7.1")
//    annotationProcessor ("android.arch.persistence.room:compiler:2.7.1")

    implementation ("com.google.code.gson:gson:2.8.9")
    implementation("de.hdodenhof:circleimageview:2.1.0")

    implementation("com.airbnb.android:lottie:3.7.0")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")


    implementation("com.github.yehiahd:FastSave-Android:1.0.6")

    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    //Multidex
    implementation("androidx.multidex:multidex:2.0.1")

    //Shimmer effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation ("com.onesignal:OneSignal:5.1.8")

    implementation(libs.billing)
    implementation(libs.espresso.core.v351)

    implementation(libs.play.services.vision)

    implementation(libs.user.messaging.platform)
    implementation(libs.play.services.ads)
    implementation(libs.audience.network.sdk)
    implementation(libs.lifecycle.process)
    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.runtime)
    //noinspection LifecycleAnnotationProcessorWithJava8
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:2.3.1")

    //FireBase
    implementation(libs.firebase.core)
    implementation(libs.firebase.config)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
}