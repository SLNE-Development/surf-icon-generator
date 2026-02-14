package dev.slne.surf.icon.generator.utils

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun String.toRgb(): Triple<Int, Int, Int> {
    val red = this.substring(0, 2).toInt(16)
    val green = this.substring(2, 4).toInt(16)
    val blue = this.substring(4, 6).toInt(16)

    return Triple(red, green, blue)
}

fun rgbToHsl(r: Int, g: Int, b: Int): Triple<Double, Double, Double> {
    val rf = r / 255.0
    val gf = g / 255.0
    val bf = b / 255.0

    val max = max(rf, max(gf, bf))
    val min = min(rf, min(gf, bf))
    val delta = max - min

    var h = 0.0
    val l = (max + min) / 2
    val s = if (delta == 0.0) 0.0 else delta / (1 - abs(2 * l - 1))

    if (delta != 0.0) {
        h = when (max) {
            rf -> ((gf - bf) / delta) % 6
            gf -> ((bf - rf) / delta) + 2
            else -> ((rf - gf) / delta) + 4
        }
        h *= 60
        if (h < 0) h += 360
    }

    return Triple(h, s, l)
}