package dev.slne.surf.icon.generator.nexo

import dev.slne.surf.icon.generator.nexo.component.Pack
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class Item(
    @Setting("material")
    val material: String,

    @Setting("itemname")
    val itemName: String,

    @Setting("Pack")
    val pack: Pack
) {
    override fun toString(): String {
        return "Item(material='$material', itemName='$itemName', pack=$pack)"
    }
}
