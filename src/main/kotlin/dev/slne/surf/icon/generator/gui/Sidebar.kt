package dev.slne.surf.icon.generator.gui

import javafx.animation.FadeTransition
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Duration

class Sidebar(
    stage: Stage,
    private val contentStack: StackPane
) : VBox() {
    private var activeButton: SidebarButton? = null

    init {
        styleClass.add("sidebar")

        val modelButton = createSidebarButton("Model Generator", ModelCard(stage))
        val configButton = createSidebarButton("Config Generator", ConfigCard(stage))

        contentStack.children.addAll(configButton.view, modelButton.view)
        contentStack.children.forEach { it.isVisible = false }

        modelButton.view.isVisible = true
        activeButton = modelButton
        activeButton?.button?.styleClass?.add("sidebar-button-active")

        children.addAll(
            Label("Surf Icon Generator").apply {
                styleClass.add("title-label")
                padding = javafx.geometry.Insets(0.0, 0.0, 20.0, 0.0)
            },
            modelButton.button,
            configButton.button
        )
    }

    fun switchView(view: SidebarButton) {
        contentStack.children.forEach { it.isVisible = false }

        view.view.opacity = 0.0
        view.view.isVisible = true

        FadeTransition(Duration.millis(250.0), view.view).apply {
            fromValue = 0.0
            toValue = 1.0
            play()
        }

        activeButton?.button?.styleClass?.remove("sidebar-button-active")
        view.button.styleClass.add("sidebar-button-active")

        activeButton = view
    }

    data class SidebarButton(
        val button: Button,
        val view: Pane,
        val sidebar: Sidebar
    ) {
        init {
            button.onAction = { _ ->
                sidebar.switchView(this)
            }
        }
    }

    private fun createSidebarButton(text: String, view: Pane): SidebarButton {
        return SidebarButton(
            Button(text).apply {
                styleClass.clear()
                stylesheets.clear()
                styleClass.add("sidebar-button")
            },
            view,
            this
        )
    }
}