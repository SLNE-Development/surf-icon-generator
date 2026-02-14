package dev.slne.surf.icon.generator.gui

import dev.slne.surf.icon.generator.utils.withGlobalStyles
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class MainApplication : Application() {
    override fun start(stage: Stage) {
        val root = BorderPane()
        val contentStack = StackPane().apply {
            styleClass.add("content")
            padding = Insets(40.0)
        }
        val scene = Scene(root, 1100.0, 650.0).withGlobalStyles()
        val sidebar = Sidebar(stage, contentStack)

        root.left = sidebar
        root.center = contentStack

        stage.title = "Surf Icon Generator"
        stage.scene = scene
        stage.show()
    }
}