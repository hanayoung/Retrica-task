package com.example.filtertoggle.filter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import com.example.filtertoggle.model.FilterType

object ColorFilter {
    val staticFilters: Map<FilterType, ColorMatrixColorFilter?> = mapOf(
        FilterType.None to null,
        FilterType.Monochrome to ColorMatrixColorFilter(ColorMatrix().apply {
            setSaturation(0f)
        }),
    )

    fun getDynamicFilter(type: FilterType, param: Float): ColorMatrixColorFilter? {
        return when (type) {
            is FilterType.Brightness -> {
                val matrix = ColorMatrix(floatArrayOf(
                    param, 0f,    0f,    0f, 0f,
                    0f,    param, 0f,    0f, 0f,
                    0f,    0f,    param, 0f, 0f,
                    0f,    0f,    0f,    1f, 0f
                ))
                ColorMatrixColorFilter(matrix)
            }
            else -> null
        }
    }
}
