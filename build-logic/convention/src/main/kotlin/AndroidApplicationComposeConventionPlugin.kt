import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    compose = true
                }
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                val bom = platform(libs.findLibrary("androidx-compose-bom").get())
                "implementation"(bom)
                "androidTestImplementation"(bom)
                "implementation"(libs.findLibrary("androidx-activity-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                "implementation"(libs.findLibrary("androidx-ui").get())
                "implementation"(libs.findLibrary("androidx-ui-graphics").get())
                "implementation"(libs.findLibrary("androidx-ui-tooling-preview").get())
                "implementation"(libs.findLibrary("androidx-material3").get())
                "androidTestImplementation"(libs.findLibrary("androidx-ui-test-junit4").get())
                "debugImplementation"(libs.findLibrary("androidx-ui-tooling").get())
                "debugImplementation"(libs.findLibrary("androidx-ui-test-manifest").get())
            }
        }
    }
}
