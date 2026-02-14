plugins {
    id("dev.slne.surf.surfapi.gradle.standalone") version "1.21.11+"
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "dev.slne.surf"
version = findProperty("version") as String

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.10.2")
}

javafx {
    version = "25.0.2"
    modules.add("javafx.controls")
}

application {
    mainClass.set("dev.slne.surf.surf.icon.generator.LauncherKt")
}