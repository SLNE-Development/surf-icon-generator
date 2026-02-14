package dev.slne.surf.icon.generator.utils

fun String.toRgb(): Triple<Int, Int, Int> {
    val red = this.substring(0, 2).toInt(16)
    val green = this.substring(2, 4).toInt(16)
    val blue = this.substring(4, 6).toInt(16)

    return Triple(red, green, blue)
}