package com.example.filtertoggle.model

sealed class FilterType {
    object None : FilterType()
    object Monochrome : FilterType()
    data class Brightness(val value: Float) : FilterType()

    val displayName: String
        get() = when (this) {
            is None -> ""
            is Monochrome -> "흑백"
            is Brightness -> "밝기 ${value}"
        }
}