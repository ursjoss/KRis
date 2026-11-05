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

dependencies {
    api(libs.rxjava.get())

    implementation(libs.bundles.kotlin)
    implementation(libs.coroutines.rx2)

    testImplementation(libs.bundles.testDeps)
    testRuntimeOnly(libs.bundles.testEngines)
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
