package com.example.filtertoggle.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filtertoggle.R
import com.example.filtertoggle.databinding.ActivityMainBinding
import com.example.filtertoggle.model.FilterInfo
import com.example.filtertoggle.model.FilterType

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val filterViewModel: FilterViewModel by viewModels()
    private lateinit var filterRVAdapter: FilterRVAdapter
    private val filterList = listOf(
        FilterInfo(FilterType.Monochrome, "흑백 필터", R.drawable.baseline_monochrome_photos_24)
    , FilterInfo(FilterType.Brightness(1.0f), "밝기 증가", R.drawable.baseline_brightness_5_24)
    , FilterInfo(FilterType.None, "원본으로", R.drawable.reset_image_24)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = filterViewModel
        }

        initAdapter()
        initData()
    }

    private fun initAdapter() {
        filterRVAdapter = FilterRVAdapter()
        binding.rvFilter.apply {
            adapter = filterRVAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        filterRVAdapter.itemClickListener = object : FilterRVAdapter.ItemClickListener {
            override fun onClick(view: View, data: FilterInfo, position: Int) {
                filterViewModel.setFilter(data.filterType)
            }
        }
    }

    private fun initData() {
        filterRVAdapter.submitList(filterList)
    }
}