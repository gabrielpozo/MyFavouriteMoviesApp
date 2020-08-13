package com.light.finder.ui.liveambiance.util

import com.light.domain.model.CctType
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageRGBFilter


object GPUImageFilterTools {

    fun createFilterForType(
        type: CctType
    ): GPUImageRGBFilter {
        val rgb = KelvinToRgbConverter.convertKelvinToRgb(type.kelvinSpec.defaultValue.toFloat())
        return GPUImageRGBFilter(
            (rgb.first / 255.0).toFloat(),
            (rgb.second / 255.0).toFloat(),
            (rgb.third / 255.0).toFloat())
    }
}