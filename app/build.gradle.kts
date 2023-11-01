plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.appdistribution")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.maxwell"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.maxwell"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            firebaseAppDistribution{
                serviceCredentialsFile = "D:/fk.json"
                releaseNotesFile="D:/releasenote.txt"
                testers="dineshkumarcse0060@gmail.com, dkcse0060@gmail.com"
            }
        }
        release {
            firebaseAppDistribution{
                serviceCredentialsFile = "D:/fk.json"
                releaseNotesFile="D:/releasenote.txt"
                testers="dineshkumarcse0060@gmail.com, dkcse0060@gmail.com"
            }
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    lint {
        warning += "InvalidPackage"
    }
}

dependencies {
    implementation("com.google.firebase:firebase-appdistribution-ktx:16.0.0-beta02")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}