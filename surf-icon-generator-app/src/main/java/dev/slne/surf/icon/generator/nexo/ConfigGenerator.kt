package dev.slne.surf.icon.generator.nexo

import dev.slne.surf.icon.generator.nexo.component.Pack
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path

@Suppress("CanBeParameter")
class ConfigGenerator(
    private val configOutputPath: () -> Path,
    private val modelOutputPath: () -> Path
) {

    private val yamlLoader = YamlConfigurationLoader.builder()
        .path(configOutputPath().resolve("icons.yml"))
        .defaultOptions { options ->
            options.serializers { builder ->
                builder.registerAnnotatedObjects(objectMapperFactory())
            }
        }
        .build()

    private val rootNode = yamlLoader.load()

    fun generateAll() {
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
        name = "surf_icon_${modelName}_tintable_base",
        item = Item(
            material = "LEATHER_HORSE_ARMOR",
            itemName = "$modelName tintable base icon",
            pack = Pack(
                model = "surf:gui/icons/${modelName}/tintable_base"
            )
        )
    )

    private fun generateTintableIconItem(modelName: String) = ItemWithName(
        name = "surf_icon_${modelName}_tintable_icon",
        item = Item(
            material = "LEATHER_HORSE_ARMOR",
            itemName = "$modelName tintable icon",
            pack = Pack(
                model = "surf:gui/icons/${modelName}/tintable_icon"
            )
        )
    )

    private data class ItemWithName(
        val name: String,
        val item: Item
    )

    private fun findAllModels(): List<String> {
        return modelOutputPath().toFile().walkTopDown()
            .filter { it.isDirectory }
            .filter { it.toPath() != modelOutputPath() }
            .map { it.name }
            .toList()
    }
}