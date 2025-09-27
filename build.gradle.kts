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
            nexusUrl.set(uri("https://central.sonatype.com/api/v1/publisher"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/api/v1/publisher/snapshots"))
            val mcUsername = providers.environmentVariable("MC_USERNAME")
            val mcPassword = providers.environmentVariable("MC_PASSWORD")
            if (mcUsername.isPresent && mcPassword.isPresent) {
                username.set(mcUsername.get())
                password.set(mcPassword.get())
            }
        }
    }
}

val kotlinSrcSet = "/src/main/kotlin"

dokka {
    dokkaSourceSets.main {
        sourceLink {
            localDirectory.set(file("$projectDir/$kotlinSrcSet"))
            remoteUrl.set(URI.create(projectRelativeSourceLink()).toURL().toURI())
            remoteLineSuffix.set("#L")
        }
    }
}

dependencies {
    dokka(project(":kris-core:"))
    dokka(project(":kris-io"))
}

tasks {
    val deleteOutFolderTask by registering(Delete::class) {
        delete("out")
    }
    named("clean") {
        delete(rootProject.layout.buildDirectory.get())
        dependsOn(deleteOutFolderTask)
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
