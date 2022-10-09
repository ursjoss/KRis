plugins {
    id("kris-detekt")
    id("kris-collect-sarif")
    id("kris-publish")
    kotlin("jvm")
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
