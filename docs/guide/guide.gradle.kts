import org.asciidoctor.gradle.jvm.AsciidoctorTask
import java.time.Year

plugins {
    alias(libs.plugins.asciidoctorConvert)
    alias(libs.plugins.asciidoctorPdf)
    alias(libs.plugins.gitPublish)
}

tasks {
    withType<AsciidoctorTask> {
        sourceDir(file("src/docs/asciidoc/"))
        setBaseDir(file("src/docs/asciidoc/"))
        sources(delegateClosureOf<PatternSet> { include("index.adoc") })
        setOutputDir(file("build/docs"))
        options(mapOf("doctype" to "book"))
        outputOptions {
            setBackends(listOf("html5", "pdf"))
            attributes = mapOf(
                "includedir" to "$sourceDir/",
                "imagesdir" to "$sourceDir/img",
                "source-highlighter" to "coderay",
                "coderay-linenums-mode" to "table",
                "rootdir" to project.rootProject.projectDir.absolutePath,
                "allow-url-read" to "",
                "toc" to "left",
                "icons" to "font",
                "encoding" to "utf-8",
                "sectlink" to "true",
                "sectanchors" to "true",
                "numbered" to "true",
                "linkattrs" to "true",
                "linkcss" to "true",
                "project-title" to "KRis -- Library for reading/writing RIS files",
                "project-inception-year" to "2017",
                "project-copyright-year" to Year.now().value,
                "project-author" to "Gianluca Colaianni, Urs Joss",
                "project-url" to "https://github.com/ursjoss/KRis",
                "project-sccm" to "https://github.com/ursjoss/KRis.git",
                "project-issue-tracker" to "https://github.com/ursjoss/KRis/issues",
                "project-group" to project.group,
                "project-version" to project.version,
                "project-name" to project.rootProject.name,
                "gradle-version" to project.gradle.gradleVersion,
            )
        }
    }

    val asciidoctor by existing
    val createGuide = register<Copy>("createGuide") {
        group = "Documentation"
        description = "Creates an Asciidoctor based guide."
        dependsOn(asciidoctor)
        destinationDir = file("${project.buildDir}/guide")
        from(project.tasks.asciidoctor.get().outputDir)
    }

    named("gitPublishCommit") {
        dependsOn(createGuide)
    }
}

gitPublish {
    branch.set("gh-pages")
    contents {
        project.tasks.findByName("createGuide")?.outputs?.files?.let {
            from(it)
        }
    }
    commitMessage.set("Publish guide for version $version")
}