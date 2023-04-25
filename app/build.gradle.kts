/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.application)
    alias(libs.plugins.dagger.hilt.android)
    kotlin("kapt")
}

android {
    // ... Application Version ...
    val majorUpdateVersion = 1
    val minorUpdateVersion = 0
    val patchVersion = 0

    val mVersionCode = majorUpdateVersion.times(10_000)
        .plus(minorUpdateVersion.times(100))
        .plus(patchVersion)

    val mVersionName = "$majorUpdateVersion.$minorUpdateVersion.$patchVersion"

    val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_a")
    val currentTime = LocalDateTime.now().format(formatter)

    val javaVersion = libs.versions.java.get().toInt()
    val compileSdkVersion = libs.versions.compileSdk.get().toInt()
    val minSdkVersion = libs.versions.minSdk.get().toInt()
    val targetSdkVersion = libs.versions.targetSdk.get().toInt()
    val composeCompilerVersion = libs.versions.androidxComposeCompiler.get().toString()

    compileSdk = compileSdkVersion

    defaultConfig {
        versionCode = mVersionCode
        versionName = mVersionName
        applicationId = "dev.atick.compose"
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName =
                    rootProject.name.replace(" ", "_") + "_" +
                        (buildType.name + "_v") +
                        (versionName + "_") +
                        "${currentTime}.apk"
                println(outputFileName)
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.values()[javaVersion - 1]
        targetCompatibility = JavaVersion.values()[javaVersion - 1]
    }

    kotlinOptions {
        jvmTarget = "$javaVersion"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    namespace = "dev.atick.compose"
}

dependencies {
    // ... Modules
    implementation(project(":core:ui"))
    implementation(project(":network"))
    implementation(project(":storage:room"))
    implementation(project(":storage:preferences"))

    // ... Dagger-Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}