package com.light.finder.ui.liveambiance.camera

abstract class CameraLoader {
    protected var previewFrameListener: OnPreviewFrameListener? = null
    abstract fun onResume(width: Int, height: Int)
    abstract fun onPause()
    abstract fun switchCamera()
    abstract val cameraOrientation: Int

    abstract fun hasMultipleCamera(): Boolean
    abstract val isFrontCamera: Boolean

    fun setOnPreviewFrameListener(onPreviewFrameListener: OnPreviewFrameListener?) {
        previewFrameListener = onPreviewFrameListener
    }

    interface OnPreviewFrameListener {
        fun onPreviewFrame(data: ByteArray?, width: Int, height: Int)
    }
}