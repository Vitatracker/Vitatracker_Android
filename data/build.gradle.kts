@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.detekt)
}

android {
    namespace = libs.versions.namespace.data.get()
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "app.mybad.data.test.HiltTestRunner"
    }

//    testOptions.targetSdk = libs.versions.sdk.compile.get().toInt()
//    lint.targetSdk = libs.versions.sdk.compile.get().toInt()

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(":core:theme"))
    implementation(project(":core:utils"))
    implementation(project(":domain"))
    implementation(project(":network"))
    implementation(project(":notifications"))

    implementation(libs.android.core.ktx)

    // Coroutines and Flow
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    //RoomDB
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Logging interceptor
    implementation(libs.okhttp.interceptor)

    // Date Time
    implementation(libs.kotlinx.datetime)

    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.work.hilt)
    ksp(libs.hilt.compiler)

    // Test
//    testImplementation(libs.junit)
//    testImplementation(project(":app"))
//
//    androidTestImplementation(libs.junit.ext)
//    androidTestImplementation(libs.junit.espresso)
//    androidTestImplementation(libs.compose.ui.test.junit4)
//
//    debugImplementation(libs.kotlinx.coroutines.test)
}
