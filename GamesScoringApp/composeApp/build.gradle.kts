import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    // Serialization is needed if you pass custom objects in Navigation
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    compilerOptions {
        // This removes the error project-wide
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            // Added: Requirement for some legacy Android components
            implementation("com.google.android.material:material:1.12.0")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Lifecycle & ViewModel (KMP versions)
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.jetbrains.navigation.compose)
            // Serialization (Replaces manual JSON parsing)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

            // Room (KMP versions)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // NOTE: Google Fonts (androidx.ui.text.google.fonts) is Android-only.
            // For KMP, put your .ttf files in commonMain/composeResources/font
            // and use Res.font.your_font_name
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.example.games_scoring_app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.games_scoring_app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    // Packaging excludes are vital for KMP to avoid duplicate file errors
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/9/previous-compilation-data.bin"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false // Set to true if you have your proguard-rules.pro ready
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)

    // CRITICAL: Room KSP processors for all targets
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}