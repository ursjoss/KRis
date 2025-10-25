import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test

class KrisDetektPluginTest {

    @Test
    fun canInstallPluginById() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("kris-detekt")
    }
}
