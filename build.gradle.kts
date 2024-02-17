// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.detekt)
    id("checkstyle")
}

buildscript {
    dependencies {
        classpath(libs.gradle.build)
        classpath(libs.hilt.plugin)
        classpath(libs.google.gms)
    }
}

dependencies {
    detektPlugins(libs.detekt)
}

detekt {
    toolVersion = detekt.toString()
    autoCorrect = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    source.setFrom(projectDir)
    buildUponDefaultConfig = false
}
true // Needed to make the Suppress annotation work for the plugins block
