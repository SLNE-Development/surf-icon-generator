package dev.slne.surf.icon.generator

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.icon.generator.commands.iconGeneratorCommand
import dev.slne.surf.icon.generator.config.IconGeneratorConfig
import dev.slne.surf.icon.generator.generator.IconGenerator
import dev.slne.surf.surfapi.core.api.config.createSpongeYmlConfig
import dev.slne.surf.surfapi.core.api.config.surfConfigApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.plugin.java.JavaPlugin
import kotlin.io.path.exists

class PaperMain : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {
        copyDefaultsIfNotExists()
    }

    override suspend fun onEnableAsync() {
        iconGeneratorCommand()
    }

    fun getIconGenerator(
        name: String,
        foregroundColor: String,
        shadowColor: String,
        backgroundColor: String,
    ) = IconGenerator(
        name = name,
        foregroundColor = foregroundColor,
        shadowColor = shadowColor,
        backgroundColor = backgroundColor,
        replaceForegroundColor = iconConfig.foregroundColor,
        replaceShadowColor = iconConfig.shadowColor,
        replaceBackgroundColor = iconConfig.backgroundColor,
        configInputPathRelative = iconConfig.configInput,
        configOutputPathRelative = iconConfig.configOutput,
        textureInputPathRelative = iconConfig.textureInput,
        textureOutputPathRelative = iconConfig.textureOutput
    )

    private suspend fun copyDefaultsIfNotExists() = withContext(Dispatchers.IO) {
        val configInputPath = dataPath.resolve(iconConfig.configInput)
        val textureInputPath = dataPath.resolve(iconConfig.textureInput)

        if (!configInputPath.exists()) {
            plugin.saveResource("input_icons.stub", false)
        }

        if (!textureInputPath.exists()) {
            plugin.saveResource("input_icons/arrow_left.png", false)
        }
    }
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)

val iconConfig
    get() = surfConfigApi.createSpongeYmlConfig<IconGeneratorConfig>(
        plugin.dataPath,
        "config.yml"
    )