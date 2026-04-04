plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":feature:reporting:domain"))
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "com.plcoding.reporting.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
}
