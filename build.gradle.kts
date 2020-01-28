plugins {
    kotlin("jvm")
    java
    id("org.kordamp.gradle.kotlin-project")
    id("org.kordamp.gradle.integration-test") apply false
    id("org.kordamp.gradle.detekt")
    id("org.kordamp.gradle.sonar")
    id("org.ajoberstar.reckon")
}

config {
    release = rootProject.findProperty("release").toString().toBoolean()

    info {
        name = "JRis"
        vendor = "Private"
        description = "Library for reading/writing RIS files"
        inceptionYear = "2017"
        organization {
            url = "https://github.com/ursjoss/JRis.git"
        }
        links {
            scm = "https://github.com/ursjoss/JRis.git"
        }
        people {
            person {
                id = "fastluca"
                name = "Gianluca Colaianni"
                roles = listOf("developer")
            }
            person {
                id = "ursjoss"
                name = "Urs Joss"
                roles = listOf("developer")
            }
        }
    }

    licensing {
        enabled = false
        licenses {
            license {
                id = "MIT"
            }
        }
    }

    quality {
        detekt {
            buildUponDefaultConfig = true
            failFast = true
        }

        sonar {
            username = "ursjoss"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
    }

    tasks {
        val deleteOutFolderTask by registering(Delete::class) {
            delete("out")
        }
        named("clean") {
            dependsOn(deleteOutFolderTask)
        }
    }
}

subprojects {
    if (project.name != "guide") {
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply<JavaPlugin>()
        apply<IdeaPlugin>()
        apply<JacocoPlugin>()

        dependencies {
            implementation(Lib.kotlin("stdlib-jdk8"))
            implementation(Lib.kotlin("reflect"))
            implementation(Lib.kotlinx("coroutines-core"))

            testImplementation(Lib.junit5("api"))
            testImplementation(Lib.spek("dsl-jvm"))
            testImplementation(Lib.mockk())
            testImplementation(Lib.kluent())
            testImplementation(Lib.assertJ())

            testRuntimeOnly(Lib.junit5("engine"))
            testRuntimeOnly(Lib.spek("runner-junit5"))
        }

        tasks {
            withType<Test> {
                @Suppress("UnstableApiUsage")
                useJUnitPlatform {
                    includeEngines("junit-jupiter", "spek2")
                }
            }
        }
    }
}

reckon {
    scopeFromProp()
    stageFromProp("beta", "rc", "final")
}