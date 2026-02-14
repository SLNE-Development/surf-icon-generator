package dev.slne.surf.icon.generator.generator.icon

import dev.slne.surf.icon.generator.generator.GeneratorResult
import dev.slne.surf.icon.generator.plugin
import dev.slne.surf.surfapi.core.api.util.toObjectList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.imageio.ImageIO
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyToRecursively
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

@Suppress("CanBeParameter")
class IconsGenerator(
    private val name: String,

    private val foregroundColor: String,
    private val shadowColor: String,
    private val backgroundColor: String,

    private val replaceForegroundColor: String,
    private val replaceShadowColor: String,
    private val replaceBackgroundColor: String,

    private val textureInputPathRelative: String,
    private val textureOutputPathRelative: String
) {
    private val textureInputPath = plugin.dataPath.resolve(textureInputPathRelative)
    private val textureOutputPath = plugin.dataPath.resolve(textureOutputPathRelative).resolve(name)

    suspend fun generate(): GeneratorResult {
        val copyResult = copyIcons()
        if (!copyResult.isSuccess()) {
            return copyResult
        }

        val replaceResult = replaceColors()
        if (!replaceResult.isSuccess()) {
            return replaceResult
        }

        return GeneratorResult.SUCCESS
    }

    @OptIn(ExperimentalPathApi::class)
    private suspend fun copyIcons(): GeneratorResult = withContext(Dispatchers.IO) {
        if (!textureInputPath.exists()) {
            return@withContext GeneratorResult.ICONS_INPUT_NOT_FOUND
        }

        textureOutputPath.createDirectories()

        try {
            textureInputPath.copyToRecursively(
                target = textureOutputPath,
                overwrite = true,
                followLinks = false
            )

            return@withContext GeneratorResult.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext GeneratorResult.ICONS_COPY_FAILED
        }
    }

    private suspend fun replaceColors(): GeneratorResult = withContext(Dispatchers.IO) {
        val files = textureOutputPath.toFile().walkTopDown().filter { it.isFile }.toObjectList()

        for (file in files) {
            val fileType = file.extension.lowercase()

            try {
                val image = ImageIO.read(file)

                for (x in 0 until image.width) {
                    for (y in 0 until image.height) {
                        val color = image.getRGB(x, y)

                        when (color) {
                            hexToArgbInt(replaceForegroundColor) -> {
                                image.setRGB(x, y, hexToArgbInt(foregroundColor))
                            }

                            hexToArgbInt(replaceShadowColor) -> {
                                image.setRGB(x, y, hexToArgbInt(shadowColor))
                            }

                            hexToArgbInt(replaceBackgroundColor) -> {
                                image.setRGB(x, y, hexToArgbInt(backgroundColor))
                            }
                        }
                    }
                }

                ImageIO.write(image, fileType, file)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext GeneratorResult.ICONS_COLOR_REPLACE_FAILED
            }
        }

        return@withContext GeneratorResult.SUCCESS
    }

    private fun hexToArgbInt(hex: String): Int {
        val rgb = hex.trim().removePrefix("#").toIntOrNull(16)
            ?: error("Invalid hexadecimal format: $hex")

        return (0xFF shl 24) or rgb
    }
}