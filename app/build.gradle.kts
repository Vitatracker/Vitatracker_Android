@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.gms)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.detekt)
}

android {
    namespace = libs.versions.namespace.app.get()
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        applicationId = libs.versions.id.get()
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.compile.get().toInt()
        versionCode = libs.versions.code.get().toInt()
        versionName = libs.versions.name.get()

        manifestPlaceholders["appAuthRedirectScheme"] = libs.versions.namespace.authRedirect.get()

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "app.mybad.data.test.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:theme"))
    implementation(project(":core:utils"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":network"))
    implementation(project(":notifications"))

    implementation(libs.android.core.ktx)
    implementation(libs.android.appcompat)

    // Lifecycle KTX
    implementation(libs.android.lifecycle.runtime.ktx)
    implementation(libs.android.lifecycle.viewmodel)
    implementation(libs.android.lifecycle.viewmodel.savedstate)

    // Compose
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))

    implementation(libs.bundles.compose.ui)

    implementation(libs.compose.activity)
    implementation(libs.compose.navigation)

    implementation(libs.compose.compiler)
    implementation(libs.compose.animation)

    implementation(libs.compose.lifecycle.viewmodel)
    implementation(libs.compose.lifecycle.runtime)

    implementation(libs.compose.material)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.material3)

    // Coroutines and Flow
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Dagger Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose.navigation)
    ksp(libs.hilt.android.compiler)

    // Coil
    implementation(libs.compose.coil)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Logging interceptor
    implementation(libs.okhttp.interceptor)

    //sign in with google
    implementation(libs.play.services.auth)

    // Date Time
    implementation(libs.kotlinx.datetime)

    // SplashScreen on devices prior Android 12
    implementation(libs.android.core.splashscreen)

    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.work.hilt)
    ksp(libs.hilt.compiler)

    // AppAuth for Android in Java
    implementation(libs.appauth)

    // Import the Firebase BoM
//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.auth)
    implementation(libs.firebase.auth.ktx)
//    implementation(libs.facebook.android.sdk)
//    implementation(libs.facebook.login)
//    implementation(libs.firebase.common.ktx)
    implementation(libs.play.services.auth)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.compose.ui.test.junit4)

    debugImplementation(libs.kotlinx.coroutines.test)
    debugImplementation(libs.bundles.compose.ui.test)
}
