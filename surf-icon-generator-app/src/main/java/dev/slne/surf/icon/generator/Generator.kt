package dev.slne.surf.icon.generator

import dev.slne.surf.icon.generator.model.ModelGenerator
import dev.slne.surf.icon.generator.nexo.ConfigGenerator
import dev.slne.surf.icon.generator.utils.Color
import java.nio.file.Path

@Suppress("CanBeParameter")
class Generator(
    private val iconBaseModelPath: () -> Path,
    private val modelInputPath: () -> Path,
    private val modelOutputPath: () -> Path,
    private val configOutputPath: () -> Path
) {
    private val modelGenerator =
        ModelGenerator(
            iconBaseModelPath,
            modelInputPath,
            modelOutputPath
        )
    private val configGenerator =
        ConfigGenerator(
            configOutputPath,
            modelOutputPath
        )

    fun generateModels() {
        modelGenerator.generateAll()
    }

    fun generateConfigs(color: Color) {
        configGenerator.generateAll(color)
    }
}