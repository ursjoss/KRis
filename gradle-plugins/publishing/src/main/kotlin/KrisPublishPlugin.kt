import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class KrisPublishPlugin : Plugin<Project> {

    private val gitUrl = "https://github.com/ursjoss/KRis"

    @Suppress("LongMethod")
    override fun apply(target: Project) {
        with (target) {
            plugins.apply("org.gradle.java-library")
            plugins.apply("com.vanniktech.maven.publish")

            extensions.configure<MavenPublishBaseExtension> {
                publishToMavenCentral(automaticRelease = true)
                signAllPublications()

                coordinates(
                    groupId = target.project.group.toString(),
                    artifactId = target.project.name,
                    version = target.project.version.toString()
                )

                pom {
                    name.set(target.rootProject.name)
                    description.set("Kotlin library for importing/exporting bibliographic records in RIS format")
                    inceptionYear.set("2017")
                    url.set(gitUrl)
                    scm {
                        url.set(gitUrl)
                    }
                    ciManagement {
                        url.set("$gitUrl/actions")
                    }
                    issueManagement {
                        url.set("$gitUrl/issues")
                    }
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/mit-license.php")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("fastluca")
                            name.set("Gianluca Colaianni")
                            roles.add("developer")
                        }
                        developer {
                            id.set("ursjoss")
                            name.set("Urs Joss")
                            roles.add("developer")
                        }
                    }
                }
            }
        }
    }
}
