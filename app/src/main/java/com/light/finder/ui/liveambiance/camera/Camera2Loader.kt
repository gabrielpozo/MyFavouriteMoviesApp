package com.light.finder.ui.liveambiance.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.util.Size
import android.view.Surface
import com.light.finder.ui.liveambiance.util.ImageUtils
import timber.log.Timber
import kotlin.math.abs

class Camera2Loader(private val activity: Activity) : CameraLoader() {
    private val cameraManager: CameraManager
    private var cameraCharacteristics: CameraCharacteristics? = null
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var imageReader: ImageReader? = null
    private var id: String? = null
    private var cameraFacing = CameraCharacteristics.LENS_FACING_BACK
    private var viewWidth = 0
    private var viewHeight = 0
    private val ratio = 0.75f // 4:3
    override fun onResume(width: Int, height: Int) {
        viewWidth = width
        viewHeight = height
        setUpCamera()
    }

    override fun onPause() {
        releaseCamera()
    }

    override fun switchCamera() {
        cameraFacing = cameraFacing xor 1
        Timber.d("current camera facing is: $cameraFacing"
        )
        releaseCamera()
        setUpCamera()
    }

    override val cameraOrientation: Int
        get() {
            var degrees = activity.windowManager.defaultDisplay.rotation
            degrees = when (degrees) {
                Surface.ROTATION_0 -> 0
                Surface.ROTATION_90 -> 90
                Surface.ROTATION_180 -> 180
                Surface.ROTATION_270 -> 270
                else -> 0
            }
            var orientation = 0
            try {
                val cameraId = getCameraId(cameraFacing)
                val characteristics =
                    cameraManager.getCameraCharacteristics(cameraId)
                orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
            Timber.d(
                "degrees: $degrees, orientation: $orientation, mCameraFacing: $cameraFacing"
            )
            return if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                (orientation + degrees) % 360
            } else {
                (orientation - degrees) % 360
            }
        }

    override fun hasMultipleCamera(): Boolean {
        try {
            val size = cameraManager.cameraIdList.size
            return size > 1
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return false
    }

    override val isFrontCamera: Boolean
        get() = cameraFacing == CameraCharacteristics.LENS_FACING_FRONT

    @SuppressLint("MissingPermission")
    private fun setUpCamera() {
        try {
            id = getCameraId(cameraFacing)
            cameraCharacteristics = cameraManager.getCameraCharacteristics(id!!)
            cameraManager.openCamera(id!!, mCameraDeviceCallback, null)
        } catch (e: CameraAccessException) {
            Timber.e(
               "Opening camera (ID: $id) failed."
            )
            e.printStackTrace()
        }
    }

    private fun releaseCamera() {
        if (captureSession != null) {
            captureSession!!.close()
            captureSession = null
        }
        if (cameraDevice != null) {
            cameraDevice!!.close()
            cameraDevice = null
        }
        if (imageReader != null) {
            imageReader!!.close()
            imageReader = null
        }
    }

    @Throws(CameraAccessException::class)
    private fun getCameraId(facing: Int): String {
        for (cameraId in cameraManager.cameraIdList) {
            if (cameraManager.getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.LENS_FACING) ==
                facing
            ) {
                return cameraId
            }
        }
        // default return
        return facing.toString()
    }

    private fun startCaptureSession() {
        val size = chooseOptimalSize()
        Timber.d("size: $size")
        imageReader = ImageReader.newInstance(
            size.width,
            size.height,
            ImageFormat.YUV_420_888,
            2
        )
        imageReader?.setOnImageAvailableListener({ reader ->
            if (reader != null) {
                val image = reader.acquireNextImage()
                if (image != null) {
                    if (previewFrameListener != null) {
                        val data: ByteArray = ImageUtils.generateNV21Data(image)
                        previewFrameListener?.onPreviewFrame(
                            data,
                            image.width,
                            image.height
                        )
                    }
                    image.close()
                }
            }
        }, null)
        try {
            cameraDevice!!.createCaptureSession(
                mutableListOf(
                    imageReader?.surface
                ), mCaptureStateCallback, null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Timber.e("Failed to start camera session")
        }
    }

    private fun chooseOptimalSize(): Size {
        Timber.d(
            "viewWidth: $viewWidth, viewHeight: $viewHeight"
        )
        if (viewWidth == 0 || viewHeight == 0) {
            return Size(0, 0)
        }
        val map =
            cameraCharacteristics!!.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val sizes = map!!.getOutputSizes(ImageFormat.YUV_420_888)
        val orientation = cameraOrientation
        val swapRotation = orientation == 90 || orientation == 270
        val width = if (swapRotation) viewHeight else viewWidth
        val height = if (swapRotation) viewWidth else viewHeight
        return getSuitableSize(sizes, width, height, ratio)
    }

    private fun getSuitableSize(
        sizes: Array<Size>,
        width: Int,
        height: Int,
        aspectRatio: Float = 0.75f
    ): Size {
        var minDelta = Int.MAX_VALUE
        var index = 0
        Timber.d(
            "getSuitableSize. aspectRatio: $aspectRatio"
        )
        for (i in sizes.indices) {
            val size = sizes[i]
            if (size.width * aspectRatio == size.height.toFloat()) {
                val delta = abs(width - size.width)
                if (delta == 0) {
                    return size
                }
                if (minDelta > delta) {
                    minDelta = delta
                    index = i
                }
            }
        }
        return sizes[index]
    }

    private val mCameraDeviceCallback: CameraDevice.StateCallback =
        object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                startCaptureSession()
            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraDevice!!.close()
                cameraDevice = null
            }

            override fun onError(camera: CameraDevice, error: Int) {
                cameraDevice!!.close()
                cameraDevice = null
            }
        }
    private val mCaptureStateCallback: CameraCaptureSession.StateCallback =
        object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                if (cameraDevice == null) {
                    return
                }
                captureSession = session
                try {
                    val builder =
                        cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    builder.addTarget(imageReader!!.surface)
                    builder.set(
                        CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                    )
                    builder.set(
                        CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                    )
                    session.setRepeatingRequest(builder.build(), null, null)
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Timber.e("Failed to configure capture session.")
            }
        }


    init {
        cameraManager =
            activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}
