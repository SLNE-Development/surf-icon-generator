package dev.slne.surf.icon.generator.model

import kotlinx.serialization.Serializable

@Serializable
data class BlockBenchModelDisplay(
    var rotation: MutableList<Float>? = null,
    var translation: MutableList<Float>? = null,
    var scale: MutableList<Float>? = null
) {
    override fun toString(): String {
        return "BlockBenchModelDisplay(rotation=$rotation, translation=$translation, scale=$scale)"
    }
}