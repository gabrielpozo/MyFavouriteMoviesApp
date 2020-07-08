package com.light.finder.ui.liveambiance

import android.os.Bundle
import android.view.View
import com.light.domain.model.FilterVariationCF
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import com.light.finder.data.source.local.LocalPreferenceDataSourceImpl
import com.light.finder.di.modules.submodules.LiveAmbianceComponent
import com.light.finder.di.modules.submodules.LiveAmbianceModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.finder.ui.adapters.FilterColorAdapter
import com.light.finder.ui.liveambiance.camera.Camera2Loader
import com.light.finder.ui.liveambiance.camera.CameraLoader
import com.light.finder.ui.liveambiance.util.GPUImageFilterTools
import com.light.presentation.viewmodels.LiveAmbianceViewModel
import com.light.source.local.LocalPreferenceDataSource
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.util.Rotation
import kotlinx.android.synthetic.main.layout_filter_dialog.*

class LiveAmbianceLightFinderActivity : BaseLightFinderActivity() {

    private lateinit var component: LiveAmbianceComponent
    private val liveAmbianceViewModel: LiveAmbianceViewModel by lazy { getViewModel { component.liveAmbianceViewModel } }
    private lateinit var filterColorAdapter: FilterColorAdapter
    private var gpuImageView: GPUImageView? = null
    private val noImageFilter = GPUImageFilter()
    private var currentImageFilter = noImageFilter
    private var cameraLoader: CameraLoader? = null
    private val localPreferences: LocalPreferenceDataSource by lazy {
        LocalPreferenceDataSourceImpl(
            this
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        component = app.applicationComponent.plus(LiveAmbianceModule())

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_ambiance)

        initView()
        initCamera()
        initAdapter()

    }

    private fun initAdapter() {
        filterColorAdapter = FilterColorAdapter(
            ::handleFilterColorPressed,
            localPreferences.loadLegendCctFilterNames()
        )
        recyclerViewColor.adapter = filterColorAdapter
    }

    private fun handleFilterColorPressed(filter: FilterVariationCF) {
        //liveAmbianceViewModel.onFilterColorTap(filter)
    }


    private fun initView() {
        gpuImageView = findViewById(R.id.gpuimage)

    }

    private fun initCamera() {
        cameraLoader = Camera2Loader(this)
        cameraLoader?.setOnPreviewFrameListener(object : CameraLoader.OnPreviewFrameListener {
            override fun onPreviewFrame(data: ByteArray?, width: Int, height: Int) {
                gpuImageView?.updatePreviewFrame(data, width, height)
            }
        })
        gpuImageView?.setRatio(0.75f)
        updateGPUImageRotate()
        gpuImageView?.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY)
    }

    private fun updateGPUImageRotate() {
        val rotation: Rotation = getRotation(cameraLoader?.cameraOrientation!!)
        var flipHorizontal = false
        var flipVertical = false
        if (cameraLoader?.isFrontCamera!!) {
            if (rotation == Rotation.NORMAL || rotation == Rotation.ROTATION_180) {
                flipHorizontal = true
            } else {
                flipVertical = true
            }
        }
        gpuImageView?.gpuImage?.setRotation(rotation, flipHorizontal, flipVertical)
    }

    private fun getRotation(orientation: Int): Rotation {
        return when (orientation) {
            90 -> Rotation.ROTATION_90
            180 -> Rotation.ROTATION_180
            270 -> Rotation.ROTATION_270
            else -> Rotation.NORMAL
        }
    }

    override fun onResume() {
        super.onResume()
        if (!gpuImageView?.isLayoutRequested!!) {
            cameraLoader?.onResume(gpuImageView?.width!!, gpuImageView?.height!!)
        } else {
            gpuImageView?.addOnLayoutChangeListener(object :
                View.OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    gpuImageView!!.removeOnLayoutChangeListener(this)
                    cameraLoader?.onResume(gpuImageView?.width!!, gpuImageView?.height!!)
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        cameraLoader?.onPause()
    }

    private val onGpuFilterClickListener: GPUImageFilterTools.OnGpuImageFilterChosenListener =
        object : GPUImageFilterTools.OnGpuImageFilterChosenListener {
            override fun onGpuImageFilterChosenListener(
                filter: GPUImageFilter?,
                filterName: String?
            ) {
                filter?.let { switchFilterTo(it) }
            }
        }


    private fun switchFilterTo(filter: GPUImageFilter) {
        //todo is this check needed?
        if (currentImageFilter.javaClass != filter.javaClass
        ) {
            currentImageFilter = filter
            gpuImageView?.filter = currentImageFilter
        }
    }
}