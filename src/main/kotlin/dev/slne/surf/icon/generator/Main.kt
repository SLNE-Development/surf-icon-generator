package dev.slne.surf.icon.generator

import com.sun.javafx.application.LauncherImpl
import dev.slne.surf.icon.generator.gui.MainApplication
import dev.slne.surf.icon.generator.utils.UserSettings
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

fun main() {
    Main.launch()
}

object Main {
    val scope =
        CoroutineScope(SupervisorJob() + CoroutineName("Main") + CoroutineExceptionHandler { context, throwable ->
            println("Coroutine exception in ${context[CoroutineName]}")
            throwable.printStackTrace()
        })

    val generator = Generator(
        iconBaseModelPath = { UserSettings.iconBaseModelFile!! },
        modelInputPath = { UserSettings.inputModelsDir!! },
        modelOutputPath = { UserSettings.outputModelsDir!! },
        configOutputPath = { UserSettings.outputConfigsDir!! }
    )

    fun launch() {
        LauncherImpl.launchApplication(MainApplication::class.java, emptyArray())
    }
}