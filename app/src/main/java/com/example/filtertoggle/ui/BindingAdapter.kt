package com.example.filtertoggle.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.filtertoggle.filter.ColorFilter
import com.example.filtertoggle.model.FilterType

object BindingAdapter {
    @BindingAdapter("colorFilter")
    @JvmStatic
    fun ImageView.selectColorFilter(filter: FilterType?) {
        if(filter is FilterType.Brightness) {
            val dynamicFilter = ColorFilter.getDynamicFilter(filter, filter.value)
            if (dynamicFilter == null) {
                clearColorFilter()
            } else {
                colorFilter = dynamicFilter
            }
        } else {
            val staticFilter = ColorFilter.staticFilters[filter]
            if (staticFilter == null) {
                clearColorFilter()
            } else {
                colorFilter = staticFilter
            }
        }
    }
}
