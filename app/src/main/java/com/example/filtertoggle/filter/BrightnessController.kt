package com.example.filtertoggle.filter

import com.example.filtertoggle.model.FilterType

class BrightnessController(private val levels: List<Float> = listOf(1.0f, 1.2f, 1.4f, 1.6f, 1.8f)) {
    private var index = 0

    val current: FilterType.Brightness
        get() = FilterType.Brightness(levels[index])

    fun next(): FilterType.Brightness {
        if (index < levels.lastIndex) index++
        return current
    }

    fun reset() {
        index = 0
    }
}
