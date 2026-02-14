package dev.slne.surf.icon.generator.model

import dev.slne.surf.surfapi.core.api.util.mutableObjectListOf
import it.unimi.dsi.fastutil.objects.ObjectList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createDirectories

class ModelGenerator(
    private val iconBaseModelPath: () -> Path,
    private val modelInputPath: () -> Path,
    private val modelOutputPath: () -> Path,
) {
    companion object {
        private val json = Json {
            prettyPrint = false
            ignoreUnknownKeys = true
        }
    }

    suspend fun generateAll() {
        val all = findAllJsons()

        all.forEach { file ->
            val modelName = file.nameWithoutExtension
            generate(modelName)
        }
    }

    suspend fun generate(modelName: String) {
        val modelPath: Path = modelOutputPath().resolve(modelName)
        modelPath.createDirectories()

        val tintableIconModel = generateTintableIconModel(modelName)
        val tintableBaseModel = generateTintableBaseModel(modelName)

        writeModel(tintableBaseModel, "tintable_base", modelPath)
        writeModel(tintableIconModel, "tintable_icon", modelPath)
    }

    private suspend fun findAllJsons(): ObjectList<File> = withContext(Dispatchers.IO) {
        val files = mutableObjectListOf<File>()

        modelInputPath().toFile().walkTopDown().forEach { file ->
            if (file.isFile && file.extension == "json") {
                files.add(file)
            }
        }

        return@withContext files
    }

    private suspend fun generateTintableIconModel(modelName: String): BlockBenchModel {
        val iconBase = decodeModel(iconBaseModelPath())
        val iconModel = decodeModel(modelInputPath().resolve("$modelName.json")).applyTint()

        iconBase.merge(iconModel)

        return iconBase
    }

    private suspend fun generateTintableBaseModel(modelName: String): BlockBenchModel {
        val iconBase = decodeModel(iconBaseModelPath()).applyTint()
        val iconModel = decodeModel(modelInputPath().resolve("$modelName.json"))

        iconBase.merge(iconModel)

        return iconBase
    }

    private suspend fun writeModel(
        model: BlockBenchModel,
        fileName: String,
        filePath: Path
    ) = withContext(Dispatchers.IO) {
        val file = filePath.resolve("$fileName.json").toFile()
        file.delete()

        file.writeText(json.encodeToString(BlockBenchModel.serializer(), model))
    }

    private suspend fun decodeModel(filePath: Path): BlockBenchModel = withContext(Dispatchers.IO) {
        val file = filePath.toFile()

        return@withContext json.decodeFromString(BlockBenchModel.serializer(), file.readText())
    }
}