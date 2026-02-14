package dev.slne.surf.icon.generator

import dev.slne.surf.icon.generator.utils.Color
import kotlinx.coroutines.runBlocking
import kotlin.io.path.Path

lateinit var main: Main
    private set

fun main(args: Array<String>) {
    main = Main(args.toList())

    runBlocking {
        main.run()
    }
}

class Main(args: List<String>) {
    val colorName = args.getOrElse(0) { "red" }.trim().replace("#", "")
    val tintColor = args.getOrElse(1) { "#ff0000" }.trim().replace("#", "")

    val dataPath = Path("runtime")

    private var modelInputPath = dataPath.resolve("input_models")
    private var modelOutputPath = dataPath.resolve("output_models")
    private var configOutputPath = dataPath.resolve("output_configs")

    private val generator = Generator(
        dataPath,
        modelInputPath,
        modelOutputPath,
        configOutputPath
    )

    suspend fun run() {
        generator.generateModels()
        generator.generateConfigs(Color.fromHex(colorName, tintColor))
    }
}