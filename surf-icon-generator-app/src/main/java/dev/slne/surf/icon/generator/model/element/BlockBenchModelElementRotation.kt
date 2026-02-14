package dev.slne.surf.icon.generator.model.element

import kotlinx.serialization.Serializable

@Serializable
data class BlockBenchModelElementRotation(
    val origin: MutableList<Float>,
    var axis: String,
    var angle: Float,
) {
    override fun toString(): String {
        return "BlockBenchModelElementRotation(origin=$origin, axis='$axis', angle=$angle)"
    }
}