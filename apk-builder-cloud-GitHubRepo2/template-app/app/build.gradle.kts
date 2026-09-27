plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.generated.webviewapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "APPLICATION_ID_PLACEHOLDER"
        minSdk = 24
        targetSdk = 34
        versionCode = VERSION_CODE_PLACEHOLDER
        versionName = "VERSION_NAME_PLACEHOLDER"
    }

    signingConfigs {
        // Free, self-contained builds: sign the "release" APK with Gradle's
        // auto-generated debug keystore so no secrets need to be configured.
        // Good for sideloading/personal distribution. For Play Store
        // distribution, replace this with a real upload keystore + GitHub
        // Actions secrets.
        getByName("debug") {
            // uses the default debug keystore Gradle creates automatically
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
