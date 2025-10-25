plugins {
    `embedded-kotlin`
    `kotlin-dsl` apply false
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

val buildTask = tasks.register("buildPlugins")

subprojects {
    buildTask.configure { dependsOn(tasks.named("build")) }
}
