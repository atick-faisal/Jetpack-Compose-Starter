plugins {
    `kotlin-dsl`
}

group = "dev.atick.build.logic"

java {
    val javaVersion = libs.versions.java.get().toInt()
    sourceCompatibility = JavaVersion.values()[javaVersion - 1]
    targetCompatibility = JavaVersion.values()[javaVersion - 1]
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("library") {
            id = "dev.atick.library"
            implementationClass = "LibraryConventionPlugin"
        }
        register("uiLibrary") {
            id = "dev.atick.ui.library"
            implementationClass = "UiLibraryConventionPlugin"
        }
        register("application") {
            id = "dev.atick.application"
            implementationClass = "ApplicationConventionPlugin"
        }
        register("daggerHilt") {
            id = "dev.atick.dagger.hilt"
            implementationClass = "DaggerHiltConventionPlugin"
        }
        register("firebase") {
            id = "dev.atick.firebase"
            implementationClass = "FirebaseConventionPlugin"
        }
    }
}