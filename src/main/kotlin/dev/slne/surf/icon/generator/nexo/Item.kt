package dev.slne.surf.icon.generator.nexo

import dev.slne.surf.icon.generator.nexo.component.Components
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class Item(
    @Setting("itemname")
    val itemName: String,

    @Setting("Components")
    val components: Components
) {
    override fun toString(): String {
        return "Item(itemName='$itemName', components=$components)"
    }
}
