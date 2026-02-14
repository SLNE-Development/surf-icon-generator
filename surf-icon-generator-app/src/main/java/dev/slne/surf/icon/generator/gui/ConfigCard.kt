package dev.slne.surf.icon.generator.gui

import dev.slne.surf.icon.generator.Main
import dev.slne.surf.icon.generator.utils.*
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.scene.paint.Color as FxColor

class ConfigCard(
    stage: Stage
) : VBox(15.0) {
    private val colorNameField: TextField
    private val colorPicker: ColorPicker

    init {
        styleClass.add("card")

        val progress = ProgressIndicator().apply { isVisible = false }
        val generate = Button("Generate Configs").apply {
            isDisable = !isConfigReady

            onAction = { _ ->
                val color = Color.Companion.fromFx(colorNameField.text, colorPicker.value)
                progress.isVisible = true
                isDisable = true

                Main.generator.generateConfigs(color)

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

        colorNameField = TextField()

        colorPicker = ColorPicker(FxColor.WHITE)
        val hexField = TextField()
        val rgbLabel = Label()
        val hslLabel = Label()

        fun updateColor(color: FxColor) {
            val r = (color.red * 255).toInt()
            val g = (color.green * 255).toInt()
            val b = (color.blue * 255).toInt()
            rgbLabel.text = "RGB: $r, $g, $b"

            val hsl =
                rgbToHsl(r, g, b)
            val h = hsl.first.toInt()
            val s = (hsl.second * 100).toInt()
            val l = (hsl.third * 100).toInt()
            hslLabel.text = "HSL: $h°, $s%, $l%"

            hexField.text = String.format("#%02X%02X%02X", r, g, b)
        }

        colorPicker.setOnAction { updateColor(colorPicker.value) }
        updateColor(colorPicker.value)

        children.addAll(
            Label("Config Generator").apply { styleClass.add("title-label") },
            labeledBox("Output Configs", outputField, browseButton),
            labeledBox("Color Name", colorNameField, null),
            Label("Color"),
            colorPicker,
            hexField,
            rgbLabel,
            hslLabel,
            generate,
            progress
        )
    }
}