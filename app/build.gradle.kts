plugins { id("com.android.application") }

android {
    namespace = "dev.gboard.quickswitch"
    compileSdk = 37

    defaultConfig {
        applicationId = "dev.gboard.quickswitch"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.0-diagnostic"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    compileOnly("io.github.libxposed:api:102.0.0")
}
