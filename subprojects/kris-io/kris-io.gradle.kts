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

configurations.configureEach {
    resolutionStrategy.eachDependency {
        if (requested.group.startsWith("org.bouncycastle")) {
            useVersion("1.86")
            because("PINNED: org.bouncycastle:bcpg-jdk18on 1.80 - org.bouncycastle:... 1.80 -> 1.85+")
        }
    }
    if (name.startsWith("dokka")) {
        resolutionStrategy.eachDependency {
            if (requested.group.startsWith("com.fasterxml.jackson") && requested.name != "jackson-annotations") {
                useVersion(rootProject.libs.versions.jackson.get())
                because("PINNED: org.jetbrains.dokka 2.3.0 - com.fasterxml.jackson.core:jackson-core 2.22.2 -> 2.22.3+")
            }
        }
    }
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

    testImplementation(libs.bundles.testDeps)
    testImplementation(libs.coroutines.test)
    testRuntimeOnly(libs.bundles.testEngines)
}

