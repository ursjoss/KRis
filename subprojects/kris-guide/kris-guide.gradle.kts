import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//    id("kris.kotlin-conventions")
    `java-library`
    kotlin("jvm")
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}

dependencies {
    testImplementation(project(":kris-io"))

    // todo centralize
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.coroutines.core)

    testImplementation(libs.junitJupiter.api)
    testImplementation(libs.spek.dsl)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
    testImplementation(libs.assertj.core)

    testRuntimeOnly(libs.junitJupiter.engine)
    testRuntimeOnly(libs.spek.runner)
}
