package dev.slne.surf.icon.generator.nexo

import dev.slne.surf.icon.generator.nexo.component.Components
import dev.slne.surf.icon.generator.utils.Color
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path
import kotlin.io.path.createDirectories

class ConfigGenerator(
    private val configOutputPath: () -> Path,
    private val modelOutputPath: () -> Path
) {
    fun generateAll(color: Color) {
        val models = findAllModels()

        models.forEach { model ->
            generate(model, color)
        }
    }

    fun generate(modelName: String, color: Color) {
        val tintableBaseItem = generateTintableBaseItem(modelName, color)
        val tintableIconItem = generateTintableIconItem(modelName, color)

        writeToYml(
            modelName,
            color,
            listOf(tintableBaseItem, tintableIconItem)
        )
    }

    private fun writeToYml(
        modelName: String,
        color: Color,
        items: List<ItemWithName>
    ) {
        val configPath = configOutputPath().resolve(modelName)
        configPath.createDirectories()

        val loader = YamlConfigurationLoader.builder()
            .path(configPath.resolve("${color.name}.yml"))
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

    private fun generateTintableBaseItem(modelName: String, color: Color) = ItemWithName(
        name = "surf_icon_${modelName}_tintable_base_${color.name}",
        item = Item(
            itemName = "${color.name} $modelName",
            components = Components(
                itemModel = "surf:models/gui/icons/${modelName}_tintable_base",
                color = color.toRgbString()
            )
        )
    )

    private fun generateTintableIconItem(modelName: String, color: Color) = ItemWithName(
        name = "surf_icon_${modelName}_tintable_icon_${color.name}",
        item = Item(
            itemName = "${color.name} $modelName Icon",
            components = Components(
                itemModel = "surf:models/gui/icons/${modelName}_tintable_icon",
                color = color.toRgbString()
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