 plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
     id("org.jetbrains.kotlin.kapt")
     id("kotlin-kapt")
     id("com.google.dagger.hilt.android")
}
android {

    namespace = "com.example.reelygoodmovies"
    compileSdk = 35
    buildFeatures{
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.reelygoodmovies"
        minSdk = 24
        targetSdk = 34
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

}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.core.ktx)
    implementation (libs.glide)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.media3.common.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    kapt (libs.hilt.android.compiler)
    implementation (libs.hilt.android.v255)
}
 kapt {
     correctErrorTypes = true
 }