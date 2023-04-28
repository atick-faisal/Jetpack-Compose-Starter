import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

@Suppress("UNUSED")
class DaggerHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                "implementation"(libs.findLibrary("dagger.hilt.android").get())
                "kapt"(libs.findLibrary("dagger.hilt.compiler").get())
            }

            configure<KaptExtension> {
                correctErrorTypes = true
            }
        }
    }
}