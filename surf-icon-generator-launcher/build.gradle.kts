import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("dev.slne.surf.surfapi.gradle.standalone")
    id("edu.sc.seis.launch4j") version "4.0.0"
    application
}

dependencies {
    implementation("org.apache.maven:maven-impl:4.0.0-rc-2")
    implementation("org.apache.maven.resolver:maven-resolver-supplier-mvn4:2.0.5")
}

surfStandaloneApi {
    addSurfApiToClasspath(false)
}

application {
    mainClass.set("dev.slne.surf.icon.generator.launcher.Launcher")
}

val appProject = project(":surf-icon-generator-app")
val appJarTask = appProject.tasks.named<ShadowJar>("shadowJar")
val disabledJarName = "app.jar.disabled"

tasks {
    val copyAppJarTask by registering(Copy::class) {
        dependsOn(appJarTask)
        from(appJarTask.flatMap { it.archiveFile })
        into(layout.buildDirectory.dir("libs"))
        rename { disabledJarName }

        inputs.file(appJarTask.flatMap { it.archiveFile })
        outputs.file(layout.buildDirectory.file("libs/$disabledJarName"))

        doFirst {
            val jar = appJarTask.get().archiveFile.get().asFile
            if (!jar.exists()) throw GradleException("App Jar not found: ${jar.absolutePath}")
        }
    }

    shadowJar {
        dependsOn(copyAppJarTask)
        from(layout.buildDirectory.file("libs/$disabledJarName"))

        manifest {
            attributes(
                "Main-Class" to application.mainClass.get()
            )
        }

        doLast {
            val file = layout.buildDirectory.file("libs/$disabledJarName").get().asFile

            if (file.exists()) file.delete()
        }
    }

    named<JavaExec>("run") {
        dependsOn(copyAppJarTask)
        doFirst {
            systemProperty(
                "launcher.appJarPath",
                layout.buildDirectory.file("libs/$disabledJarName").get().asFile.absolutePath
            )
        }
    }
    launch4j {
        mainClassName = application.mainClass.get()
        setJarTask(named("shadowJar"))
        outputDir.set(
            rootProject.layout.buildDirectory.dir("../executables").get().asFile.absolutePath
        )
        outfile.set("surf-icon-generator-launcher.exe")
        icon = "$projectDir/src/main/resources/icon.ico"
        dontWrapJar.set(false)
        jreMinVersion.set("25")
        stayAlive.set(true)
        headerType.set("console")
    }
}