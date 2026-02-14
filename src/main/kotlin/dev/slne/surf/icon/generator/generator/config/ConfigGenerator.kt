package dev.slne.surf.icon.generator.generator.config

import dev.slne.surf.icon.generator.generator.GeneratorResult
import dev.slne.surf.icon.generator.plugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val STUB_NAME = "{{name}}"
private const val STUB_TEXTURE_OUTPUT_DIRECTORY = "{{texture_output_directory}}"

@Suppress("CanBeParameter")
class ConfigGenerator(
    private val name: String,

    private val inputPathRelative: String,
    private val outputPathRelative: String,
    private val nexoTexturePath: String,
) {
    private val inputPath = plugin.dataPath.resolve(inputPathRelative)
    private val outputPath = plugin.dataPath.resolve(outputPathRelative)

    private val inputFile = inputPath.toFile()
    private val outputFile = outputPath.resolve("$name.yml").toFile()

    suspend fun generate(): GeneratorResult {
        val copyResult = copyConfigTemplate()
        if (!copyResult.isSuccess()) {
            return copyResult
        }

        val stubResult = replaceStubs()
        if (!stubResult.isSuccess()) {
            return stubResult
        }

        return GeneratorResult.SUCCESS
    }

    private suspend fun copyConfigTemplate(): GeneratorResult = withContext(Dispatchers.IO) {
        if (!inputFile.exists()) {
            return@withContext GeneratorResult.CONFIG_INPUT_NOT_FOUND
        }

        try {
            inputFile.copyTo(outputFile, overwrite = true)
            return@withContext GeneratorResult.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext GeneratorResult.CONFIG_COPY_FAILED
        }
    }

    private fun replaceStubs(): GeneratorResult {
        if (!outputFile.exists()) {
            return GeneratorResult.CONFIG_OUTPUT_NOT_FOUND
        }

        try {
            var content = outputFile.readText()

            content = content.replace(STUB_NAME, name)
            content = content.replace(STUB_TEXTURE_OUTPUT_DIRECTORY, nexoTexturePath)

            outputFile.writeText(content)

            return GeneratorResult.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            return GeneratorResult.CONFIG_STUB_REPLACE_FAILED
        }
    }
}