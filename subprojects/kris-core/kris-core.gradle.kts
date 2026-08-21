import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kris-detekt")
    id("kris-collect-sarif")
    id("kris-publish")
    id("kris-jacoco")
    kotlin("jvm")
    alias(libs.plugins.dokka)
    alias(libs.plugins.powerAssert)
}

dependencies {
    api(libs.rxjava.get())

    implementation(libs.bundles.kotlin)
    implementation(libs.coroutines.rx2)

    testImplementation(libs.bundles.testDeps)
    testRuntimeOnly(libs.bundles.testEngines)
}

configurations.configureEach {
    if (name.startsWith("dokka")) {
        resolutionStrategy.eachDependency {
            if (requested.group.startsWith("com.fasterxml.jackson")) {
                useVersion(libs.versions.jackson.get())
                because("CVE fix: pin all Jackson to 2.22.2")
            }
            if (requested.group.startsWith("org.jsoup")) {
                useVersion(libs.versions.jsoup.get())
                because("CVE fix: pin jsoup to 1.23.1")
            }
        }
    }
}

kotlin {
    explicitApi()
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
powerAssert {
    functions = listOf(
        "io.kotest.matchers.shouldBe",
    )
}

dokka {
    dokkaSourceSets {
        named("main") {
            includes.from("module.md")
        }
    }
}

tasks {
    val apiBuild = getByName("apiBuild")
    named("jacocoTestReport") {
        dependsOn(apiBuild)
    }
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
