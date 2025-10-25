plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.detekt)
    alias(libs.plugins.sonarqube)
}

dependencies {
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.sonarqube)
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

detekt {
    buildUponDefaultConfig = true
    config.from(file("../../config/detekt.yml"))
}

testing {
    @Suppress("UnstableApiUsage", "unused")
    suites {
        val test by getting(JvmTestSuite::class) {
            useKotlinTest()
        }
    }
}

gradlePlugin {
    plugins {
        create("kris-detekt") {
            id = "kris-detekt"
            implementationClass = "KrisDetektPlugin"
        }
        create("kris-collect-sarif") {
            id = "kris-collect-sarif"
            implementationClass = "CollectSarifPlugin"
        }
        create("kris-jacoco") {
            id = "kris-jacoco"
            implementationClass = "KrisJacocoPlugin"
        }
    }
}
