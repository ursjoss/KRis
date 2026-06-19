plugins {
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

dependencies {
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.maven.publish)
}

detekt {
    buildUponDefaultConfig = true
    config.from(file("../../config/detekt.yml"))
}

testing {
    @Suppress("UnstableApiUsage", "unused")
    suites {
        val test = getByName<JvmTestSuite>("test") {
            useKotlinTest()
        }
    }
}

gradlePlugin {
    plugins {
        create("kris-publish") {
            id = "kris-publish"
            implementationClass = "KrisPublishPlugin"
        }
    }
}
