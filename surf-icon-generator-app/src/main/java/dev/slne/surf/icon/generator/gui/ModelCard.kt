package dev.slne.surf.icon.generator.gui

import dev.slne.surf.icon.generator.Main
import dev.slne.surf.icon.generator.utils.*
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Stage

class ModelCard(
    stage: Stage
) : VBox(15.0) {
    init {
        styleClass.add("card")

        val baseIconModelField = TextField(UserSettings.iconBaseModelFile?.toString() ?: "").apply {
            isEditable = false
        }

        val inputField = TextField(UserSettings.inputModelsDir?.toString() ?: "").apply {
            isEditable = false
        }

        val outputField = TextField(UserSettings.outputModelsDir?.toString() ?: "").apply {
            isEditable = false
        }

        val progress = ProgressIndicator().apply { isVisible = false }

        val generate = Button("Generate Models").apply {
            isDisable = !isConfigReady

            onAction = { _ ->
                progress.isVisible = true
                isDisable = true

                Main.generator.generateModels()

                progress.isVisible = false
                isDisable = false

                Alert(
                    Alert.AlertType.INFORMATION,
                    "Models generated successfully!"
                ).showAndWait()
            }
        }

        val baseIconButton = Button("Browse")
        baseIconButton.setOnAction {
            chooseFile(stage, UserSettings.iconBaseModelFile)?.let {
                baseIconModelField.text = it.toString()
                UserSettings.iconBaseModelFile = it
                generate.isDisable = !generate.isModelReady
            }
        }

        val inputButton = Button("Browse")
        inputButton.setOnAction {
            chooseDirectory(stage, UserSettings.inputModelsDir)?.let {
                inputField.text = it.toString()
                UserSettings.inputModelsDir = it
                generate.isDisable = !generate.isModelReady
            }
        }

        val outputButton = Button("Browse")
        outputButton.setOnAction {
            chooseDirectory(stage, UserSettings.outputModelsDir)?.let {
                outputField.text = it.toString()
                UserSettings.outputModelsDir = it
                generate.isDisable = !generate.isModelReady
            }
        }

        children.addAll(
            Label("Model Generator").apply { styleClass.add("title-label") },
            labeledBox("Base Icon Model", baseIconModelField, baseIconButton),
            labeledBox("Input Models", inputField, inputButton),
            labeledBox("Output Models", outputField, outputButton),
            generate,
            progress
        )
    }
}