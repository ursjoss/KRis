plugins {
    id("kris-detekt")
    id("kris-collect-sarif")
    id("kris-publish")
    id("kris-jacoco")
    kotlin("jvm")
    alias(libs.plugins.kotest)
    alias(libs.plugins.dokka)
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

dokka {
    dokkaSourceSets {
        named("main") {
            includes.from("module.md")
        }
    }
}

tasks {
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
        dependsOn(apiBuild)
    }
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
