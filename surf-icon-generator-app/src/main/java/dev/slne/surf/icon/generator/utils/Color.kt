package dev.slne.surf.icon.generator.utils

import javafx.scene.paint.Color as FxColor

data class Color(
    val name: String,
    val r: Int,
    val g: Int,
    val b: Int,
) {
    fun toRgbString() = "$r, $g, $b"

    companion object {
        fun fromFx(name: String, fxColor: FxColor): Color {
            require(name.isNotEmpty()) { "Color name cannot be empty" }

            val r = (fxColor.red * 255).toInt()
            val g = (fxColor.green * 255).toInt()
            val b = (fxColor.blue * 255).toInt()

            return Color(name, r, g, b)
        }

        fun fromRgb(name: String, r: Int, g: Int, b: Int): Color {
            require(name.isNotEmpty()) { "Color name cannot be empty" }
            require(r in 0..255) { "Red value must be between 0 and 255" }
            require(g in 0..255) { "Green value must be between 0 and 255" }
            require(b in 0..255) { "Blue value must be between 0 and 255" }

            return Color(name, r, g, b)
        }

        fun fromHex(name: String, hex: String): Color {
            val cleanHex = hex.trim().removePrefix("#")
            require(cleanHex.length == 6) { "Hex color must be 6 characters long" }

            val rgb = cleanHex.toRgb()

            return fromRgb(name, rgb.first, rgb.second, rgb.third)
        }
    }
}
