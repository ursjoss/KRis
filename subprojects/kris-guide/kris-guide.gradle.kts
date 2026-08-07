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
    // work around sonar issue documented in https://github.com/bc-lee/sonarqube-gradle-jvm-resolver-poc
    named("sonarResolver") {
        dependsOn(":kris-io:compileKotlin", ":kris-core:compileKotlin")
    }
}
