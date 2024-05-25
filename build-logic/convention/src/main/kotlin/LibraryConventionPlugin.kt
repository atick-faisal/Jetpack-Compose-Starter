import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val javaVersion = libs.findVersion("java").get().toString()
            val minSdkVersion = libs.findVersion("minSdk").get().toString().toInt()
            val compileSdkVersion = libs.findVersion("compileSdk").get().toString().toInt()

            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.dokka")
            }

            extensions.configure<LibraryExtension> {

                compileSdk = compileSdkVersion

                defaultConfig {
                    minSdk = minSdkVersion
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.valueOf("VERSION_$javaVersion")
                    targetCompatibility = JavaVersion.valueOf("VERSION_$javaVersion")
                }

            }

            extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(javaVersion))
                }
            }
        }
    }
}