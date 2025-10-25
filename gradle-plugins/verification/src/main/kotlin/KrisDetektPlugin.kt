import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class KrisDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            plugins.apply("kotlin")
            plugins.apply("org.sonarqube")
            plugins.apply("io.gitlab.arturbosch.detekt")
            plugins.withId("io.gitlab.arturbosch.detekt") {
                val rootProject = rootProject

                // specify base path to make file locations relative
                extensions.configure<DetektExtension> {
                    buildUponDefaultConfig = true
                    allRules = true
                    baseline = file("detekt-baseline.xml")
                    basePath = rootProject.projectDir.absolutePath
                    config.setFrom("${rootProject.projectDir}/config/detekt.yml")
                }

                // enable SARIF report
                val detektTask = tasks.named("detekt", Detekt::class.java)
                detektTask.configure {
                    reports.sarif.required.set(true)
                    reports.xml.required.set(true)
                }

                // add detekt output to inputs of ReportMergeTask
                // mustRunAfter should be used here otherwise the merged report won't be generated on fail
                rootProject.plugins.withId("kris-collect-sarif") {
                    rootProject.tasks.named(
                        CollectSarifPlugin.MERGE_DETEKT_TASK_NAME,
                        ReportMergeTask::class.java
                    ) {
                        input.from(detektTask.map { it.sarifReportFile }.orNull)
                        mustRunAfter(detektTask)
                    }
                }
            }
            rootProject.tasks.named("sonarqube") {
                dependsOn(tasks.getByName("detekt"))
            }
            rootProject.tasks.named("check") {
                dependsOn(tasks.getByName("detektMain"))
            }
        }
    }
}
