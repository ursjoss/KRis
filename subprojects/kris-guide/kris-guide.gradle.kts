plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    testImplementation(project(":kris-io"))

    implementation(libs.bundles.kotlin)

    testImplementation(libs.bundles.testDeps)
    testRuntimeOnly(libs.bundles.testEngines)
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
