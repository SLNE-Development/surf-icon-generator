package dev.slne.surf.icon.generator.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class IconGeneratorConfig(
    val textureInput: String = "input_icons",
    val textureOutput: String = "output_icons",
    val nexoTexturePath: String = "surf:textures/gui/icons",
    val configInput: String = "input_icons.stub",
    val configOutput: String = "output_configs",

    val foregroundColor: String = "#FFFFFF",
    val shadowColor: String = "#CCCCCC",
    val backgroundColor: String = "#000000"
)