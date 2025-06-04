plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.myphotogallery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myphotogallery"
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

// app/build.gradle.kts
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // RecyclerView for grid display
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ViewPager2 for swipe navigation in full-screen
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // annotationProcessor("com.github.bumptech.glide:compiler:4.16.0") // For Java

    // ViewModel and LiveData (Lifecycle components)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")
    implementation("androidx.activity:activity-ktx:1.9.0") // For by viewModels() delegate
}