package com.light.finder.ui.liveambiance

import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import com.light.finder.di.modules.submodules.LiveAmbianceComponent
import com.light.finder.di.modules.submodules.LiveAmbianceModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.finder.ui.liveambiance.camera.Camera2Loader
import com.light.finder.ui.liveambiance.camera.CameraLoader
import com.light.finder.ui.liveambiance.util.GPUImageFilterTools
import com.light.presentation.viewmodels.LiveAmbianceViewModel
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class LiveAmbianceLightFinderActivity : BaseLightFinderActivity() {

    private lateinit var component: LiveAmbianceComponent
    private val liveAmbianceViewModel: LiveAmbianceViewModel by lazy { getViewModel { component.liveAmbianceViewModel } }

    private var gpuImageView: GPUImageView? = null
    private val noImageFilter = GPUImageFilter()
    private var currentImageFilter = noImageFilter
    private var cameraLoader: CameraLoader? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        component = app.applicationComponent.plus(LiveAmbianceModule())

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_ambiance)

        initView()
        initCamera()

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
        gpuImageView?.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY)
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

    private val mOnGpuImageFilterChosenListener: GPUImageFilterTools.OnGpuImageFilterChosenListener =
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