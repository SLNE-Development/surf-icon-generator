package dev.slne.surf.icon.generator.gui

import dev.slne.surf.icon.generator.Main
import dev.slne.surf.icon.generator.utils.UserSettings
import dev.slne.surf.icon.generator.utils.chooseDirectory
import dev.slne.surf.icon.generator.utils.isConfigReady
import dev.slne.surf.icon.generator.utils.labeledBox
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Stage

class ConfigCard(
    stage: Stage
) : VBox(15.0) {
    init {
        styleClass.add("card")

        val progress = ProgressIndicator().apply { isVisible = false }
        val generate = Button("Generate Configs").apply {
            isDisable = !isConfigReady

            onAction = { _ ->
                progress.isVisible = true
                isDisable = true

                Main.generator.generateConfigs()

                progress.isVisible = false
                isDisable = false

                Alert(
                    Alert.AlertType.INFORMATION,
                    "Configs generated successfully!"
                ).showAndWait()
            }
        }

        val outputField = TextField(UserSettings.outputConfigsDir?.toString() ?: "").apply {
            isEditable = false
        }

        val browseButton = Button("Browse")
        browseButton.setOnAction {
            chooseDirectory(stage, UserSettings.outputConfigsDir)?.let {
                outputField.text = it.toString()
                UserSettings.outputConfigsDir = it
                generate.isDisable = generate.isConfigReady
            }
        }

        children.addAll(
            Label("Config Generator").apply { styleClass.add("title-label") },
            labeledBox("Output Configs", outputField, browseButton),
            Label("Color"),
            generate,
            progress
        )
    }
}