package dev.slne.surf.icon.generator.utils

import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.stage.Window
import java.nio.file.Path

val Button.isConfigReady: Boolean
    get() = isModelReady && UserSettings.outputConfigsDir != null

val Button.isModelReady: Boolean
    get() = UserSettings.outputConfigsDir != null &&
            UserSettings.iconBaseModelFile != null &&
            UserSettings.inputModelsDir != null

fun Pane.labeledBox(label: String, field: TextField, button: Button?): VBox {
    field.prefWidth = 400.0

    val hBox = if (button != null) {
        HBox(10.0, field, button)
    } else {
        HBox(10.0, field)
    }

    return VBox(5.0, Label(label), hBox.apply {
        alignment = Pos.CENTER_LEFT
    })
}

fun Pane.chooseDirectory(stage: Stage, initialPath: Path?): Path? {
    return DirectoryChooser().apply {
        if (initialPath != null) {
            initialDirectory = initialPath.toFile()
        }
    }.showDialog(stage)?.toPath()
}

fun Pane.chooseFile(window: Window, initialPath: Path?): Path? {
    return FileChooser().apply {
        if (initialPath != null) {
            initialDirectory = initialPath.parent.toFile()
        }
    }.showOpenDialog(window)?.toPath()
}

fun Scene.withGlobalStyles(): Scene {
    stylesheets.add(javaClass.getResource("/theme.css")!!.toExternalForm())
    return this
}