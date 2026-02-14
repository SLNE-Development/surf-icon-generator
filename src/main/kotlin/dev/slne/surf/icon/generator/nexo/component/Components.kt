package dev.slne.surf.icon.generator.nexo.component

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class Components(
    @Setting("item_model")
    val itemModel: String
)
