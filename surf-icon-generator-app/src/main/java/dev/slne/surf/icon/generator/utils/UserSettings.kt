package dev.slne.surf.icon.generator.utils

import java.nio.file.Path
import java.util.prefs.Preferences

object UserSettings {
    private val preferences = Preferences.userRoot().node("dev/slne/surf/icon/generator")

    var iconBaseModelFile: Path?
        get() = preferences.get("iconBaseModelFile", null)?.let { Path.of(it) }
        set(value) {
            if (value == null) {
                preferences.remove("iconBaseModelFile")
                return
            }

            preferences.put("iconBaseModelFile", value.toString())
        }

    var inputModelsDir: Path?
        get() = preferences.get("inputModelsDir", null)?.let { Path.of(it) }
        set(value) {
            preferences.put("inputModelsDir", value?.toString())
        }

    var outputModelsDir: Path?
        get() = preferences.get("outputModelsDir", null)?.let { Path.of(it) }
        set(value) {
            preferences.put("outputModelsDir", value?.toString())
        }

    var outputConfigsDir: Path?
        get() = preferences.get("outputConfigsDir", null)?.let { Path.of(it) }
        set(value) {
            preferences.put("outputConfigsDir", value?.toString())
        }
}