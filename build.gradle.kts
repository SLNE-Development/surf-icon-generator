plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin") version "1.21.11+"
}

group = "dev.slne.surf"
version = findProperty("version") as String

surfPaperPluginApi {
    mainClass("dev.slne.surf.icon.generator.PaperMain")
    authors.addAll("Ammo")
    foliaSupported(true)
    generateLibraryLoader(false)
}