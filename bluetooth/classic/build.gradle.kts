plugins {
    id("dev.atick.library")
    id("dev.atick.dagger.hilt")
}

android {
    namespace = "dev.atick.bluetooth.classic"
}

dependencies {
    implementation(project(":core:android"))
    api(project(":bluetooth:common"))
}