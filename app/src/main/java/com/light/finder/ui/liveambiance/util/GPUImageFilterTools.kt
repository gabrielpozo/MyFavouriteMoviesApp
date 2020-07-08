package com.light.finder.ui.liveambiance.util

import android.content.Context
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageRGBFilter
import java.util.*


object GPUImageFilterTools {
    fun addToAdapter(
        context: Context,
        listener: OnGpuImageFilterChosenListener
    ) {
        val filters = FilterList()
        filters.addFilter("WARM", FilterType.WARM)
        filters.addFilter("WARM WHITE", FilterType.WARM_WHITE)
        filters.addFilter("SOFT WHITE", FilterType.SOFT_WHITE)
        filters.addFilter("COLD WHITE", FilterType.COLD_WHITE)
        filters.addFilter("COOL", FilterType.COOL)
        filters.addFilter("DAYLIGHT", FilterType.DAYLIGHT)


    }

    //todo get the kelvin values from sharedpref and convert to rgb
    private fun createFilterForType(
        context: Context,
        type: FilterType
    ): GPUImageFilter {
        return when (type) {
            FilterType.WARM -> {
                GPUImageRGBFilter(80.toFloat(), 45.toFloat(), 7.toFloat())
            }
            FilterType.WARM_WHITE -> {
                GPUImageRGBFilter(
                    1.0f,
                    0.58f,
                    0.16f
                )
            }
            FilterType.SOFT_WHITE -> {
                GPUImageRGBFilter(
                    0.33f,
                    0.66f,
                    1.0f
                )
            }
            FilterType.DAYLIGHT -> {
                GPUImageRGBFilter(
                    0.6f,
                    0.81f,
                    1.0f
                )
            }
            FilterType.COOL -> {
                GPUImageRGBFilter(
                    0.8f,
                    0.89f,
                    1.0f
                )
            }
            FilterType.COLD_WHITE -> {
                GPUImageRGBFilter(1.0f, 1.0f, 1.0f)
            }
        }
    }

    interface OnGpuImageFilterChosenListener {
        fun onGpuImageFilterChosenListener(
            filter: GPUImageFilter?,
            filterName: String?
        )
    }

    private enum class FilterType {
        WARM, WARM_WHITE, SOFT_WHITE, COLD_WHITE, COOL, DAYLIGHT
    }

    private class FilterList {
        var names: MutableList<String> =
            LinkedList()
        var filters: MutableList<FilterType> = LinkedList()
        fun addFilter(name: String, filter: FilterType) {
            names.add(name)
            filters.add(filter)
        }
    }
}