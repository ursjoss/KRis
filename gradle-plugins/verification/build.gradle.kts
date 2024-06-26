import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

dependencies {
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.detekt)
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

detekt {
    buildUponDefaultConfig = true
    config.from(file("../../config/detekt.yml"))
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
