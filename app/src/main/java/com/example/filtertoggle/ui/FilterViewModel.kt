package com.example.filtertoggle.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filtertoggle.filter.BrightnessController
import com.example.filtertoggle.model.FilterType

class FilterViewModel: ViewModel() {
    val selectedFilter = MutableLiveData<FilterType>(FilterType.None)
    private val brightnessController = BrightnessController()

    fun setFilter(filter: FilterType) {
        if(filter is FilterType.Brightness) {
            brightnessController.next()
            selectedFilter.value = brightnessController.current
        } else if(selectedFilter.value == filter && filter == FilterType.Monochrome) {
            selectedFilter.value = FilterType.None
        } else {
            selectedFilter.value = filter
            brightnessController.reset()
        }
    }
}

