plugins {
    alias(libs.plugins.android.application)
    // 1. AGREGA ESTA LÍNEA AQUÍ PARA ACTIVAR FIREBASE:
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.afinal"
    compileSdk = 34 // (Ojo: Si te da error con el '36' o 'release', usa 34 que es el estable actual. Si te funciona el 36, déjalo)

    defaultConfig {
        applicationId = "com.example.afinal"
        minSdk = 24
        targetSdk = 34 // Igual aquí, usa 34 para asegurar compatibilidad
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Librerías que ya tenías (no las toques)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // 2. AGREGA ESTAS LÍNEAS AL FINAL PARA EL EXAMEN (FIREBASE Y GLIDE):
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}