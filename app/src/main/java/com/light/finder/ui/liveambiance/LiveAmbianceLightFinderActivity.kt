package com.light.finder.ui.liveambiance

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.jakewharton.processphoenix.ProcessPhoenix
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import com.light.finder.data.source.local.LocalPreferenceDataSourceImpl
import com.light.finder.data.source.remote.CctTypeParcelable
import com.light.finder.di.modules.submodules.LiveAmbianceComponent
import com.light.finder.di.modules.submodules.LiveAmbianceModule
import com.light.finder.extensions.*
import com.light.finder.ui.adapters.LiveAmbianceAdapter
import com.light.finder.ui.liveambiance.camera.Camera2Loader
import com.light.finder.ui.liveambiance.camera.CameraLoader
import com.light.finder.ui.liveambiance.util.GPUImageFilterTools
import com.light.finder.ui.splash.SplashLightFinderActivity
import com.light.presentation.viewmodels.LiveAmbianceViewModel
import com.light.source.local.LocalPreferenceDataSource
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.util.Rotation
import kotlinx.android.synthetic.main.activity_live_ambiance.*
import kotlinx.android.synthetic.main.ambiance_snackbar.*
import kotlin.system.exitProcess


class LiveAmbianceLightFinderActivity : BaseLightFinderActivity() {

    companion object {
        const val LIVE_AMBIANCE_ID_KEY = "LiveAmbianceActivity::id"
        const val CCT_LIST_EXTRA = "ccTListId"
        const val REQUEST_CODE_AMBIANCE = 1
        const val CAMERA_REQUEST_CODE = 101

    }

    private lateinit var component: LiveAmbianceComponent
    private val liveAmbianceViewModel: LiveAmbianceViewModel by lazy { getViewModel { component.liveAmbianceViewModel } }
    private lateinit var filterColorAdapter: LiveAmbianceAdapter
    private var gpuImageView: GPUImageView? = null
    private val noImageFilter = GPUImageFilter()
    private var currentImageFilter = noImageFilter
    private var cameraLoader: CameraLoader? = null
    private val localPreferences: LocalPreferenceDataSource by lazy {
        LocalPreferenceDataSourceImpl(
            this
        )
    }
    private var isHasPermission = true


    override fun onCreate(savedInstanceState: Bundle?) {
        component = app.applicationComponent.plus(LiveAmbianceModule())

        intent.getParcelableArrayListExtra<CctTypeParcelable>(LIVE_AMBIANCE_ID_KEY)
            ?.let { cctList ->
                liveAmbianceViewModel.onRetrieveCctList(
                    cctList.deparcelizeCctList()
                )
            }

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_ambiance)

        liveAmbianceViewModel.model.observe(this, Observer { uiModel -> updateUI(uiModel) })
        liveAmbianceViewModel.modelList.observe(this, Observer(::setColorAdapter))

        initView()

        initCamera()


    }

    private fun restartApp() {

        val intent = Intent(this, SplashLightFinderActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
        /*      if (context is Activity) {
                  (context as Activity).finish()
              }*/
        Runtime.getRuntime().exit(0)
    }

    private fun initDisclaimerText() {
        if (localPreferences.isDisclaimerAccepted()) {
            return
        }
        ok_button.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
        custom_toast.fadeIn()

        ok_button.setOnClickListener {
            custom_toast.fadeOut()
            localPreferences.disclaimerAccepted(true)
        }
    }


    private fun setColorAdapter(colorList: LiveAmbianceViewModel.ContentColors) {
        filterColorAdapter = LiveAmbianceAdapter(
            liveAmbianceViewModel::onFilterClick,
            colorList.cctList
        )
        recyclerViewFilter.adapter = filterColorAdapter
    }


    private fun updateUI(model: LiveAmbianceViewModel.Content) {
        switchFilterTo(GPUImageFilterTools.createFilterForType(model.filter))
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
        //gpuImageView?.setRatio(0.75f)
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
        if (isHasPermission) {
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
    }

    override fun onStart() {
        super.onStart()
        initDisclaimerText()
        Log.d("Gabriel", "onStart!")


        isHasPermission = checkSelfCameraPermission()
        if (!isHasPermission) {
            Log.d("Gabriel", "it hasn't permission!!")
            //ProcessPhoenix.triggerRebirth(this)
            //restartApp()
        }

    }

    override fun onPause() {
        super.onPause()
        cameraLoader?.onPause()
    }


    private fun switchFilterTo(filter: GPUImageFilter) {
        currentImageFilter = filter
        gpuImageView?.filter = currentImageFilter

    }

    override fun onBackPressed() {
        setIntentForResult {
            putExtra(
                CCT_LIST_EXTRA,
                liveAmbianceViewModel.onRetrievingColorSelected()
            )
        }
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }

}