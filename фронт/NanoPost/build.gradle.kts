plugins {
    val kotlinVersion = "1.5.0"

    kotlin("android") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.35")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}