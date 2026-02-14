package dev.slne.surf.icon.generator.generator

import dev.slne.surf.icon.generator.generator.config.ConfigGenerator
import dev.slne.surf.icon.generator.generator.icon.IconsGenerator

@Suppress("CanBeParameter")
class IconGenerator(
    private val name: String,

    private var foregroundColor: String,
    private var shadowColor: String,
    private var backgroundColor: String,

    private var replaceForegroundColor: String,
    private var replaceShadowColor: String,
    private var replaceBackgroundColor: String,

    private val configInputPathRelative: String,
    private val configOutputPathRelative: String,
    private val textureInputPathRelative: String,
    private val textureOutputPathRelative: String,

    private val nexoTexturePath: String,
) {
    init {
        foregroundColor = foregroundColor.trim().removePrefix("#")
        shadowColor = shadowColor.trim().removePrefix("#")
        backgroundColor = backgroundColor.trim().removePrefix("#")

        replaceForegroundColor = replaceForegroundColor.trim().removePrefix("#")
        replaceShadowColor = replaceShadowColor.trim().removePrefix("#")
        replaceBackgroundColor = replaceBackgroundColor.trim().removePrefix("#")
    }

    private val configGenerator = ConfigGenerator(
        name = name,
        inputPathRelative = configInputPathRelative,
        outputPathRelative = configOutputPathRelative,
        nexoTexturePath = nexoTexturePath
    )

    private val iconsGenerator = IconsGenerator(
        name = name,
        foregroundColor = foregroundColor,
        shadowColor = shadowColor,
        backgroundColor = backgroundColor,
        replaceForegroundColor = replaceForegroundColor,
        replaceShadowColor = replaceShadowColor,
        replaceBackgroundColor = replaceBackgroundColor,
        textureInputPathRelative = textureInputPathRelative,
        textureOutputPathRelative = textureOutputPathRelative
    )

    suspend fun generate(): GeneratorResult {
        val validationResult = checkInputs(name, foregroundColor, shadowColor, backgroundColor)

        if (!validationResult.isSuccess()) {
            return validationResult
        }

        val configResult = configGenerator.generate()
        if (!configResult.isSuccess()) {
            return configResult
        }

        val iconsResult = iconsGenerator.generate()
        if (!iconsResult.isSuccess()) {
            return iconsResult
        }

        return GeneratorResult.SUCCESS
    }

    private fun checkInputs(
        name: String,
        foregroundColor: String,
        shadowColor: String,
        backgroundColor: String
    ): GeneratorResult {
        if (name.isBlank()) return GeneratorResult.INVALID_NAME

        if (!isValidHexColor(foregroundColor)) return GeneratorResult.INVALID_FOREGROUND_COLOR
        if (!isValidHexColor(shadowColor)) return GeneratorResult.INVALID_SHADOW_COLOR
        if (!isValidHexColor(backgroundColor)) return GeneratorResult.INVALID_BACKGROUND_COLOR

        return GeneratorResult.SUCCESS
    }

    @Suppress("PrivatePropertyName")
    private val HEX_REGEX = Regex("^[0-9a-fA-F]{6}$")

    private fun isValidHexColor(color: String): Boolean {
        return color.matches(HEX_REGEX)
    }
}