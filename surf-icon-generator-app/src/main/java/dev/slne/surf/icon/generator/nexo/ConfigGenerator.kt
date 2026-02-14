package dev.slne.surf.icon.generator.nexo

import dev.slne.surf.icon.generator.nexo.component.Pack
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path

class ConfigGenerator(
    private val configOutputPath: () -> Path,
    private val modelOutputPath: () -> Path
) {
    fun generateAll() {
        val models = findAllModels()

        models.forEach { model ->
            generate(model)
        }
    }

    fun generate(modelName: String) {
        val tintableBaseItem = generateTintableBaseItem(modelName)
        val tintableIconItem = generateTintableIconItem(modelName)

        writeToYml(
            modelName,
            listOf(tintableBaseItem, tintableIconItem)
        )
    }

    private fun writeToYml(
        modelName: String,
        items: List<ItemWithName>
    ) {
        val loader = YamlConfigurationLoader.builder()
            .path(configOutputPath().resolve("${modelName}.yml"))
            .defaultOptions { options ->
                options.serializers { builder ->
                    builder.registerAnnotatedObjects(objectMapperFactory())
                }
            }
            .build()

        val root = loader.createNode()
        items.forEach { (name, item) ->
            root.node(name).set(item)
        }

        loader.save(root)
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