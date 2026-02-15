plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp) // ✅ garde KSP pour Room
}

android {
    namespace = "com.exemple.sonicflow"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.exemple.sonicflow"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas".toString()
                )
            }
        }
    }

    buildFeatures {
        compose = true
    }

    kotlin {
        jvmToolchain(17)
    }
}


dependencies {
    // ✅ Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    // ✅ Navigation
    implementation(libs.navigation.compose)

    // ✅ ViewModel
    implementation(libs.lifecycle.viewmodel.compose)

    // ✅ Room (via KSP)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // ✅ Media3
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
    implementation(libs.media3.common)
    implementation("androidx.media3:media3-ui:1.3.1")
}
