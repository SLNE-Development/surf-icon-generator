package dev.slne.surf.icon.generator.model.element

import dev.slne.surf.icon.generator.model.Direction
import kotlinx.serialization.Serializable

@Serializable
data class BlockBenchModelElementFace(
    val uv: MutableList<Float>,
    var texture: String,
    var rotation: Int? = null,
    var cullface: Direction? = null,
    var tintindex: Int? = null
) {
    override fun toString(): String {
        return "BlockBenchModelElementFace(uv=$uv, texture='$texture', cullface=$cullface, tintindex=$tintindex)"
    }
}
