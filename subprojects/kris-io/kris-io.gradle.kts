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
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
        @Suppress("unused")
        val integrationTest by registering(JvmTestSuite::class) {
            testType.set(TestSuiteType.INTEGRATION_TEST)
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
        }
    }
}

tasks {
    named("check") {
        dependsOn(testing.suites.named("integrationTest"))
    }
    val javadocJar by existing(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Assembled Javadoc JAR"
        archiveClassifier.set("javadoc")
        from(named("dokkaGenerate"))
    }
    named("sourcesJar") {
        dependsOn(javadocJar)
    }
    val apiBuild by existing
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
    testRuntimeOnly(libs.bundles.testEngines)
}
