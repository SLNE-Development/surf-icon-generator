package dev.slne.surf.icon.generator.nexo

import dev.slne.surf.icon.generator.nexo.component.Pack
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteIfExists

@Suppress("CanBeParameter")
class ConfigGenerator(
    private val configOutputPath: () -> Path,
    private val modelOutputPath: () -> Path
) {
    private val configPath = configOutputPath().resolve("icons.yml")

    private val yamlLoader = YamlConfigurationLoader.builder()
        .path(configPath)
        .defaultOptions { options ->
            options.serializers { builder ->
                builder.registerAnnotatedObjects(objectMapperFactory())
            }
        }
        .build()

    private val rootNode = yamlLoader.load()

    fun generateAll() {
        configPath.deleteIfExists()
        configPath.createParentDirectories()
        configPath.createFile()

        val models = findAllModels()

        models.forEach { model ->
            generate(model)
        }
    }

    fun generate(modelName: String) {
        val tintableBaseItem = generateTintableBaseItem(modelName)
        val tintableIconItem = generateTintableIconItem(modelName)

        saveNewModel(tintableBaseItem, tintableIconItem)
    }

    private fun saveNewModel(
        tintableBaseItem: ItemWithName,
        tintableIconItem: ItemWithName
    ) {
        writeToYml(listOf(tintableBaseItem, tintableIconItem))
    }

    private fun writeToYml(items: List<ItemWithName>) {
        items.forEach { (name, item) ->
            rootNode.node(name).set(item)
        }

        yamlLoader.save(rootNode)
    }

    private fun generateTintableBaseItem(modelName: String) = ItemWithName(
        name = "surf_icon_${modelName}",
        item = Item(
            material = "LEATHER_HORSE_ARMOR",
            itemName = "$modelName tintable base icon",
            pack = Pack(
                model = "surf:gui/icons/${modelName}"
            )
        )
    )

    private fun generateTintableIconItem(modelName: String) = ItemWithName(
        name = "surf_icon_${modelName}",
        item = Item(
            material = "LEATHER_HORSE_ARMOR",
            itemName = "$modelName tintable icon",
            pack = Pack(
                model = "surf:gui/icons/${modelName}"
            )
        )
    )

    private data class ItemWithName(
        val name: String,
        val item: Item
    )

    private fun findAllModels(): List<String> {
        return modelOutputPath().toFile().walkTopDown()
            .filter { it.toPath() != modelOutputPath() }
            .map { it.nameWithoutExtension }
            .toList()
    }
}