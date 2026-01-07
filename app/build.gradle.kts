plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

android {
    namespace = "com.example.core"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.testhomework"
        minSdk = 32
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)


    // Для логирования запросов (опционально)
    implementation (libs.logging.interceptor)

    // Room

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    
    // Compose dependencies
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material) // For SwipeToDismiss
    implementation(libs.androidx.activity.compose)
    
    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    
    // ViewModel Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)


    //Koin
    implementation(libs.koin)
    implementation(libs.koin.androidx.compose)



    // Тестирование

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)

    // MockK
    testImplementation(libs.mockk)
    // Для тестирования ViewModel с LiveData/StateFlow
    testImplementation(libs.androidx.core.testing)

    // Если используете LiveData в тестах:
    testImplementation(libs.androidx.lifecycle.livedata.ktx)

//Junit 5 для Assertions так как уже написал на 4
    testImplementation ("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
}