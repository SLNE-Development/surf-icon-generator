package dev.slne.surf.icon.generator.model

import kotlinx.serialization.Serializable

@Serializable
data class BlockBenchModelGroup(
    var name: String,
    val origin: MutableList<Float>,
    val children: MutableList<Int>,
    var color: Int? = null
) {
    fun move(x: Float, y: Float, z: Float) {
        origin[0] += x
        origin[1] += y
        origin[2] += z
    }

    fun offsetChildren(offset: Int) {
        for (i in children) {
            children[i] += offset
        }
    }

    override fun toString(): String {
        return "BlockBenchModelGroup(name='$name', origin=$origin, children=$children)"
    }
}
