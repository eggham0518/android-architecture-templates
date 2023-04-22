import Deps.LifeCycle.lifecycleLiveDataKtx
import Deps.LifeCycle.lifecycleViewModelKtx
import Versions.room_version
import org.jetbrains.kotlin.gradle.plugin.extraProperties

/*
* Copyright 2021 Marco Cattaneo
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    compileSdk = AppConfiguration.COMPILE_SDK
    namespace = AppConfiguration.APPLICATION_ID


    defaultConfig {
        applicationId = AppConfiguration.APPLICATION_ID
        minSdk = AppConfiguration.MIN_SDK
        targetSdk = AppConfiguration.TARGET_SDK
        versionCode = AppConfiguration.VERSION_CODE
        versionName = AppConfiguration.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.kotlinCompilerExtension
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    with(Deps.Compose) {
        implementation(ui)
        implementation(material)
        implementation(icon)

        implementation(preview)
        implementation(preview_tooling)

        implementation(viewModel)
        implementation(navigation)
    }

    with(Deps.AndroidX) {
        implementation(activityCompose)
        implementation(activityKtx)
    }

    with(Deps.Hilt) {
        implementation(hiltNavigatoinCompose)
        implementation(hiltAndroid)
        kapt(hiltAndroidCompiler)

    }

    with(Deps.LifeCycle) {
        implementation(lifecycleRuntimeKtx)
        implementation(lifecycleLiveDataKtx)
        implementation(lifecycleViewModelKtx)
    }

    with(Deps.Room) {
        implementation(room_runtime)
        annotationProcessor(room_runtime_compiler)
        // To use Kotlin annotation processing tool (kapt)
        kapt(room_runtime_compiler)
        // optional - Kotlin Extensions and Coroutines support for Room
        implementation(room_runtime_ktx)
    }

    implementation(Deps.Timber.timber)
}