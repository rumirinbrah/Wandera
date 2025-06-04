import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.devtools.ksp) apply  false
}
/*
fun BaseExtension.getDefaultConfig(){
    compileSdkVersion(35)

    defaultConfig {
        minSdk = 26
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
fun PluginContainer.applyDefaultConfig(project: Project){
    whenPluginAdded {
        when(this){
            is AppPlugin->{
                project.extensions.getByType<AppExtension>()
                    .apply {
                        getDefaultConfig()
                    }
            }
            is LibraryPlugin->{
                project.extensions.getByType<LibraryExtension>()
                    .apply {
                        getDefaultConfig()
                    }
            }
        }
    }
}
subprojects {
    project.plugins.applyDefaultConfig(project)
}

 */
