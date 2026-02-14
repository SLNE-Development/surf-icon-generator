plugins {
    id("dev.slne.surf.surfapi.gradle.standalone") version "1.21.11+"
    application
}

group = "dev.slne.surf"
version = findProperty("version") as String

application {
    mainClass.set("dev.slne.surf.surf.icon.generator.MainKt")
}