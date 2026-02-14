package dev.slne.surf.icon.generator

import com.sun.javafx.application.LauncherImpl
import dev.slne.surf.icon.generator.gui.MainApplication
import dev.slne.surf.icon.generator.utils.UserSettings
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object Main {
    private val scope =
        CoroutineScope(SupervisorJob() + CoroutineName("Main") + CoroutineExceptionHandler { context, throwable ->
            println("Coroutine exception in ${context[CoroutineName]}")
            throwable.printStackTrace()
        })

    fun launch(
        context: CoroutineContext = scope.coroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        if (!scope.isActive) {
            return Job()
        }

        return scope.launch(context, start, block)
    }

    val generator = Generator(
        iconBaseModelPath = { UserSettings.iconBaseModelFile!! },
        modelInputPath = { UserSettings.inputModelsDir!! },
        modelOutputPath = { UserSettings.outputModelsDir!! },
        configOutputPath = { UserSettings.outputConfigsDir!! }
    )

    fun run() {
        LauncherImpl.launchApplication(MainApplication::class.java, emptyArray())
    }
}