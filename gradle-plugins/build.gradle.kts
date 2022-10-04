//buildscript {
//    repositories {
//        mavenCentral()
//    }
//    dependencies {
//        classpath(libs.plugin.kotlin)
//        classpath(libs.plugin.kotlin)
//    }
//}
//
//plugins {
//    kotlin("jvm") version libs.versions.kotlin.get()
//}

val buildTask = tasks.register("buildPlugins")

subprojects {
    buildTask.configure { dependsOn(tasks.named("build")) }
}
