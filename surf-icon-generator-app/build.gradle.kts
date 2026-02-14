plugins {
    id("dev.slne.surf.surfapi.gradle.core")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.3.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlin:kotlin-serialization:2.3.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
}

javafx {
    version = "25.0.2"
    modules.add("javafx.controls")
}

application {
    mainClass.set("dev.slne.surf.icon.generator.Main")
}