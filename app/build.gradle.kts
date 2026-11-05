import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val propiedades = Properties()
val archivoLocal = rootProject.file("local.properties")

if (archivoLocal.exists()) {
    archivoLocal.inputStream().use {
        propiedades.load(it)
    }
}

val claveMapa = propiedades
    .getProperty("MAPTILER_API_KEY", "")
    .trim()

android {
    namespace = "com.example.perfilinteractivo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.perfilinteractivo"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "MAPTILER_API_KEY",
            "\"$claveMapa\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")

    implementation(
        platform("androidx.compose:compose-bom:2025.06.01")
    )

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("org.osmdroid:osmdroid-android:6.1.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
