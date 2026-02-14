package dev.slne.surf.icon.generator.commands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.icon.generator.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun iconGeneratorCommand() = commandAPICommand("icongenerator") {
    stringArgument("name")
    textArgument("foregroundColor")
    textArgument("shadowColor")
    textArgument("backgroundColor")

    anyExecutor { sender, arguments ->
        val name: String by arguments
        val foregroundColor: String by arguments
        val shadowColor: String by arguments
        val backgroundColor: String by arguments

        plugin.launch {
            sender.sendText {
                appendInfoPrefix()
                info("Generating icon with name ")
                variableValue(name)
                info(", foreground color ")
                variableValue(foregroundColor)
                info(", shadow color ")
                variableValue(shadowColor)
                info(" and background color ")
                variableValue(backgroundColor)
                info("...")
            }

            val generator = plugin.getIconGenerator(
                name = name,
                foregroundColor = foregroundColor,
                shadowColor = shadowColor,
                backgroundColor = backgroundColor,
            )

            val result = generator.generate()

            if (!result.isSuccess()) {
                sender.sendText {
                    appendErrorPrefix()
                    error("Failed to generate icon: ")
                    variableValue(result.name)
                }

                return@launch
            } else {
                sender.sendText {
                    appendSuccessPrefix()
                    success("Successfully generated icon with name ")
                    variableValue(name)
                    success(", foreground color ")
                    variableValue(foregroundColor)
                    success(", shadow color ")
                    variableValue(shadowColor)
                    success(" and background color ")
                    variableValue(backgroundColor)
                    success(".")
                }
            }
        }
    }
}