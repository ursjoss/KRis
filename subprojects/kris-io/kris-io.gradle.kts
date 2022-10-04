import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//    id("kris.kotlin-conventions")
    id("kris-detekt")
    id("kris-collect-sarif")
    `java-library`
    kotlin("jvm")
    alias(libs.plugins.testSets)
}

testSets {
    create("integrationTest")
}

tasks {
    named("check") {
        dependsOn("integrationTest")
    }
}

dependencies {
    api(project(":kris-core"))

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
