plugins {
    kotlin("jvm") version "1.7.20"
    `kotlin-dsl`
    id("com.github.ben-manes.versions") version "0.42.0"
}

repositories {
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    implementation("com.github.ben-manes:gradle-versions-plugin:0.42.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
}
