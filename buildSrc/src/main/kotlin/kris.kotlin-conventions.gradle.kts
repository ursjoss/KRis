import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin
}

group = "ch.difty.kris"

repositories {
    mavenCentral()
}

//
//reckon {
//    scopeFromProp()
//    stageFromProp("beta", "rc", "final")
//}
//
val javaVersion = JavaVersion.VERSION_11
val kotlinVersion = "1.7.20"

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

kotlin {
    explicitApi()
}


tasks {
    val deleteOutFolderTask by registering(Delete::class) {
        delete("out")
    }
    named("clean").configure {
        dependsOn(deleteOutFolderTask)
    }
    val kotlinApiLangVersion = kotlinVersion.subSequence(0, 3).toString()
    val jvmTargetVersion = javaVersion.toString()
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            apiVersion = kotlinApiLangVersion
            languageVersion = kotlinApiLangVersion
            jvmTarget = jvmTargetVersion
            freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
        }
    }
    withType<JavaCompile>().configureEach {
        sourceCompatibility = jvmTargetVersion
        targetCompatibility = jvmTargetVersion
    }
    withType<Test> {
        @Suppress("UnstableApiUsage")
        useJUnitPlatform {
            includeEngines("junit-jupiter", "spek2")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.19")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("org.amshove.kluent:kluent:1.68")
    testImplementation("org.assertj:assertj-core:3.23.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.19")
}
