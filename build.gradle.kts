import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URI

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.plugin.kotlin)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    id("kris-collect-sarif")
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.dokka)
    alias(libs.plugins.nexusPublish)
    alias(libs.plugins.binaryCompatValidator)
    `maven-publish`
    jacoco
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.projectKey", "ursjoss_${project.name}")
        property("sonar.organization", "ursjoss-github")
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            val ossrhUsername = providers.environmentVariable("OSSRH_USERNAME")
            val ossrhPassword = providers.environmentVariable("OSSRH_PASSWORD")
            if (ossrhUsername.isPresent && ossrhPassword.isPresent) {
                username.set(ossrhUsername.get())
                password.set(ossrhPassword.get())
            }
        }
    }
}

val kotlinSrcSet = "/src/main/kotlin"

tasks {
    val deleteOutFolderTask by registering(Delete::class) {
        delete("out")
    }
    named("clean") {
        delete(rootProject.layout.buildDirectory.get())
        dependsOn(deleteOutFolderTask)
    }
    val dokkaHtml by getting(DokkaTask::class) {
        dokkaSourceSets {
            configureEach {
                sourceLink {
                    localDirectory.set(file("$projectDir/$kotlinSrcSet"))
                    remoteUrl.set(URI.create(projectRelativeSourceLink()).toURL())
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }
}

subprojects.forEach { subProject ->
    if (subProject.name in setOf("kris-core", "kris-io")) {
        apply {
            plugin("org.sonarqube")
            plugin("jacoco")
        }
        sonarqube {
            properties {
                property(
                    "sonar.kotlin.detekt.reportPaths",
                    subProject.layout.buildDirectory.get().asFile.resolve("reports/detekt/detekt.xml")
                )
                property(
                    "sonar.coverage.jacoco.xmlReportPaths",
                    subProject.layout.buildDirectory.get().asFile.resolve("reports/jacoco/test/jacocoTestReport.xml")
                )
            }
        }
    }

    subProject.tasks {
        named("compileKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class.java) {
            val kotlinApiLangVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.fromVersion(libs.versions.kotlin.get().take(3))
            compilerOptions {
                apiVersion = kotlinApiLangVersion
                languageVersion = kotlinApiLangVersion
                freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            }
        }
        withType<Test> {
            useJUnitPlatform {
                includeEngines("junit-jupiter", "kotest")
            }
        }
        withType<DokkaTaskPartial>().configureEach {
            dokkaSourceSets {
                configureEach {
                    includes.from("module.md")
                }
            }
        }
    }
}

// see https://github.com/detekt/detekt/issues/6198
configurations.matching { it.name == "detekt" }.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(libs.versions.detektKotlinVersion.get())
        }
    }
}


fun Project.projectRelativeSourceLink(branch: String = "main", srcSet: String = kotlinSrcSet) =
    "https://github.com/ursjoss/KRis/blob/$branch/${projectDir.relativeTo(rootDir)}/$srcSet"

fun String.mayHaveTestCoverage(): Boolean = startsWith("kris")
