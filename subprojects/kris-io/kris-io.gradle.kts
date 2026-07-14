@file:Suppress("UnstableApiUsage")

plugins {
    id("kris-detekt")
    id("kris-collect-sarif")
    id("kris-publish")
    id("kris-jacoco")
    kotlin("jvm")
    alias(libs.plugins.dokka)
    `jvm-test-suite`
}

kotlin {
    explicitApi()
}

dokka {
    dokkaSourceSets {
        named("main") {
            includes.from("module.md")
        }
    }
}

testing {
    suites {
        val test = getByName<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
        @Suppress("unused")
        val integrationTest = register<JvmTestSuite>("integrationTest") {
            dependencies {
                implementation(project())
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
        configurations.named("integrationTestImplementation") {
            extendsFrom(configurations.testImplementation.get())
            dependencies {
                implementation(libs.bundles.testEngines)
            }
        }
    }
}

tasks {
    named("check") {
        dependsOn(testing.suites.named("integrationTest"))
    }
    val apiBuild = getByName("apiBuild")
    named("jacocoTestReport") {
        dependsOn(testing.suites.named("integrationTest"))
        dependsOn(apiBuild)
        rootProject.subprojects.firstOrNull { it.name == "kris-core" }?.tasks?.named("test")?.let {
            dependsOn(it)
        }
        rootProject.subprojects.firstOrNull { it.name == "kris-core" }?.tasks?.named("apiBuild")?.let {
            mustRunAfter(it)
        }
    }
}

dependencies {
    api(project(":kris-core"))

    implementation(libs.bundles.kotlin)
    implementation(libs.jackson.core)
    implementation(libs.jackson.module.kotlin)

    testImplementation(libs.bundles.testDeps)
    testImplementation(libs.coroutines.test)
    testRuntimeOnly(libs.bundles.testEngines)
}
