import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test

class CollectSarifPluginTest {

    @Test
    fun canInstallPluginById() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("kris-collect-sarif")
    }
}
