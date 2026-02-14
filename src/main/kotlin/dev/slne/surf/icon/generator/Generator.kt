package dev.slne.surf.icon.generator

import dev.slne.surf.icon.generator.model.ModelGenerator
import dev.slne.surf.icon.generator.nexo.ConfigGenerator
import dev.slne.surf.icon.generator.utils.Color
import java.nio.file.Path

@Suppress("CanBeParameter")
class Generator(
    private val dataPath: Path,
    private val modelInputPath: Path,
    private val modelOutputPath: Path,
    private val configOutputPath: Path
) {
    private val modelGenerator = ModelGenerator(dataPath, modelInputPath, modelOutputPath)
    private val configGenerator = ConfigGenerator(configOutputPath, modelOutputPath)

    suspend fun generateModels() {
        modelGenerator.generateAll()
    }

    suspend fun generateConfigs(color: Color) {
        configGenerator.generateAll(color)
    }
}