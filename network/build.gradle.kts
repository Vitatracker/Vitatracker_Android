@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.gms)
    alias(libs.plugins.detekt)
}

android {
    namespace = libs.versions.namespace.network.get()
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation(libs.android.core.ktx)

    // Coroutines and Flow
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Dagger Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose.navigation)
    ksp(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Logging interceptor
    implementation(libs.okhttp.interceptor)

    // Date Time
    implementation(libs.kotlinx.datetime)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.facebook.android.sdk)
    implementation(libs.facebook.login)
    implementation(libs.firebase.common.ktx)
    implementation(libs.play.services.auth)

    // AppAuth for Android in Java
    implementation(libs.appauth)
    implementation(libs.browser)
}
