package dev.slne.surf.icon.generator.model.element

import dev.slne.surf.icon.generator.model.Direction
import kotlinx.serialization.Serializable

@Serializable
data class BlockBenchModelElement(
    val from: MutableList<Float>,
    val to: MutableList<Float>,
    val rotation: BlockBenchModelElementRotation? = null,
    val faces: MutableMap<Direction, BlockBenchModelElementFace>
) {
    fun move(x: Float, y: Float, z: Float): BlockBenchModelElement {
        from[0] += x
        from[1] += y
        from[2] += z

        to[0] += x
        to[1] += y
        to[2] += z

        return this
    }

    fun applyTint(index: Int = 0) {
        for (face in faces.values) {
            face.tintindex = index
        }
    }

    override fun toString(): String {
        return "BlockBenchModelElement(from=$from, to=$to, rotation=$rotation, faces=$faces)"
    }
}