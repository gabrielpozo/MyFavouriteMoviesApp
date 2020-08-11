package com.light.finder.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Process
import android.provider.MediaStore
import android.provider.Settings
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.common.util.concurrent.ListenableFuture
import com.light.domain.model.Message
import com.light.domain.model.ParsingError
import com.light.finder.R
import com.light.finder.common.ActivityCallback
import com.light.finder.common.ConnectivityRequester
import com.light.finder.common.InternetUtil
import com.light.finder.common.PermissionRequester
import com.light.finder.data.source.local.ImageRepository
import com.light.finder.data.source.remote.reports.CrashlyticsException
import com.light.finder.di.modules.submodules.CameraComponent
import com.light.finder.di.modules.submodules.CameraModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.CameraViewModel
import com.light.presentation.viewmodels.CameraViewModel.*
import kotlinx.android.synthetic.main.camera_ui_container.*
import kotlinx.android.synthetic.main.camera_ui_container.view.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.gallery_preview_layout.*
import kotlinx.android.synthetic.main.layout_permission.*
import kotlinx.android.synthetic.main.layout_preview.*
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

class CameraFragment : BaseFragment() {

    private lateinit var component: CameraComponent
    private val viewModel: CameraViewModel by lazy { getViewModel { component.cameraViewModel } }
    private val imageRepository: ImageRepository by lazy { component.imageRepository }
    private lateinit var cameraPermissionRequester: PermissionRequester
    private lateinit var galleryPermissionRequester: PermissionRequester
    private lateinit var connectivityRequester: ConnectivityRequester
    private lateinit var activityCallback: ActivityCallback
    private lateinit var alertDialog: AlertDialog
    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraProvider: ProcessCameraProvider
    private var controls: View? = null
    private var modelUiState: ModelStatus = ModelStatus.FEED


    val timer = object : CountDownTimer(INIT_INTERVAL, DOWN_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            val timeText = millisUntilFinished / 1000
            if (timeText > 0) {
                countDownText?.text = timeText.toString()
            }
        }

        override fun onFinish() {

        }
    }

    companion object {
        private const val TAG = "CameraX"
        private const val INIT_INTERVAL = 9000L
        private const val DOWN_INTERVAL = 1000L
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val ANIMATION_FAST_MILLIS = 50L
        private const val ANIMATION_SLOW_MILLIS = 100L
        private const val TIME_OUT_LOG_REPORT = 408
        private const val PARSE_ERROR_LOG_REPORT = 422
        private const val REQUEST_IMAGE_GET = 1
        private var flashMode = ImageCapture.FLASH_MODE_OFF
    }

    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }


    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService


    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: PreviewView

    //private lateinit var outputDirectory: File
    private lateinit var broadcastManager: LocalBroadcastManager
    //private lateinit var mainExecutor: Executor

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    private var isComingFromSettings: Boolean = false
    private var isGalleryDenied: Boolean = false

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@CameraFragment.displayId) {
                imageCapture?.targetRotation = view.display.rotation
                imageAnalyzer?.targetRotation = view.display.rotation
            }
        } ?: Unit
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mainExecutor = ContextCompat.getMainExecutor(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as ActivityCallback
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_camera, container, false)


    private val imageCaptureListener = object : ImageCapture.OnImageCapturedCallback() {
        override fun onError(exception: ImageCaptureException) {
            super.onError(exception)
            displayCameraItemsControl()
            //TODO(reinitilize the camera here in case something went wrong)
            Timber.e("$TAG Photo capture failed: $exception cause")
        }

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun onCaptureSuccess(image: ImageProxy) {
            viewModel.onCameraButtonClicked(
                imageRepository.getBitmap(image.image!!),
                image.imageInfo.rotationDegrees
            )
            image.close()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            component = lightFinderComponent.plus(CameraModule())
            cameraPermissionRequester = PermissionRequester(this, Manifest.permission.CAMERA)
            galleryPermissionRequester =
                PermissionRequester(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            connectivityRequester = ConnectivityRequester(this)
        } ?: throw Exception("Invalid Activity")

        container = view as ConstraintLayout
        viewFinder = container.findViewById(R.id.viewFinder)
        viewFinder.preferredImplementationMode = PreviewView.ImplementationMode.TEXTURE_VIEW

        viewModel.model.observe(viewLifecycleOwner, Observer(::observeUpdateUI))
        viewModel.modelPreview.observe(viewLifecycleOwner, Observer(::observePreviewView))
        viewModel.modelRequest.observe(viewLifecycleOwner, Observer(::observeModelContent))
        viewModel.modelRequestCancelOrRestore.observe(viewLifecycleOwner, Observer(::observeCancelRestoreRequest))
        viewModel.modelItemCountRequest.observe(viewLifecycleOwner, Observer(::observeItemCount))
        viewModel.modelDialog.observe(viewLifecycleOwner, Observer(::observeErrorResponse))
        viewModel.modelResponseDialog.observe(
            viewLifecycleOwner,
            Observer(::observeDialogButtonAction)

        )
        viewModel.modelGallery.observe(viewLifecycleOwner, Observer(::observeModelGallery))
        viewModel.modelNavigationFiltering.observe(
            viewLifecycleOwner,
            Observer(::observeNavigationFiltering)
        )


        requestItemCount()
        setBrowsingClickable()

        displayManager.registerDisplayListener(displayListener, null)
        broadcastManager = LocalBroadcastManager.getInstance(view.context)

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()


    }

    private fun setBrowsingClickable() {
        browseButton.setOnClickListener {
            if (InternetUtil.isInternetOn()) {
                viewModel.onBrowsingButtonClicked()
            } else {
                activityCallback.onInternetConnectionLost()
            }

        }

    }

    private fun observeModelGallery(model: Event<GalleryViewDisplay>) {
        model.getContentIfNotHandled()?.let {
            pickImageFromGallery()
            pickLatestFromGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val file = File(uri.path!!)
                val rotation = getExifOrientation(context?.contentResolver?.openInputStream(uri))
                initGalleryPreviewUI(uri, rotation)
                setGalleryPreviewListeners(uri, rotation)
            }
        } else {
            hideGalleryPreview()
        }
    }

    private fun initGalleryPreviewUI(uri: Uri, rotation: Int) {
        // ui
        layoutPreviewGallery.visible()
        cameraUiContainer.gone()
        browseButton.gone()
        activityCallback.setBottomBarInvisibility(true)
        galleryPreview.setImageURI(uri)
        galleryPreview.rotation = rotation.toFloat()
        modelUiState = ModelStatus.GALLERY
    }

    private fun setGalleryPreviewListeners(uri: Uri, rotation: Int) {
        confirmPhoto.setOnClickListener {
            screenNavigator.toGalleryPreview(this)
            if (InternetUtil.isInternetOn()) {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
               val inputStream=  activity?.contentResolver?.openInputStream(uri)


                inputStream?.let {
                    val bitmapImage = decodeSampledBitmapFromResourceMemOpt(inputStream!!, 800, 800)
                    viewModel.onCameraButtonClicked(bitmapImage!!, rotation)
                    layoutPreviewGallery.gone()
                    modelUiState = ModelStatus.FEED
                }
                /*       val bitmapImage =
                    BitmapFactory.decodeStream(activity?.contentResolver?.openInputStream(uri))
                viewModel.onCameraButtonClicked(bitmapImage, rotation)
                layoutPreviewGallery.gone()
                modelUiState = ModelStatus.FEED*/

            } else {
                activityCallback.onInternetConnectionLost()
            }

        }

        cancelPhoto.setOnClickListener {
            screenNavigator.toGalleryPreview(this)
            hideGalleryPreview()
            browseButton.visible()
        }

        setBrowsingClickable()

    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun decodeSampledBitmapFromResourceMemOpt(
        inputStream: InputStream, reqWidth: Int, reqHeight: Int
    ): Bitmap? {
        var byteArr = ByteArray(0)
        val buffer = ByteArray(1024)
        var len: Int
        var count = 0
        return try {
            while (inputStream.read(buffer).also { len = it } > -1) {
                if (len != 0) {
                    if (count + len > byteArr.size) {
                        val newbuf = ByteArray((count + len) * 2)
                        System.arraycopy(byteArr, 0, newbuf, 0, count)
                        byteArr = newbuf
                    }
                    System.arraycopy(buffer, 0, byteArr, count, len)
                    count += len
                }
            }
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(byteArr, 0, count, options)
            options.inSampleSize = calculateInSampleSize(
                options, reqWidth,
                reqHeight
            )
            options.inPurgeable = true
            options.inInputShareable = true
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val pids = intArrayOf(Process.myPid())
       /*     val myMemInfo: MemoryInfo = mAM.getProcessMemoryInfo(pids).get(0)
            Log.e(TAG, "dalvikPss (decoding) = " + myMemInfo.dalvikPss)*/
            BitmapFactory.decodeByteArray(byteArr, 0, count, options)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun getExifOrientation(filepath: InputStream?): Int {
        var degree = 0
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(filepath!!)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        if (exif != null) {
            val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            if (orientation != -1) {
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                }
            }
        }
        return degree
    }

    private fun hideGalleryPreview() {
        modelUiState = ModelStatus.FEED
        activityCallback.setBottomBarInvisibility(false)
        layoutPreviewGallery.gone()
        cameraUiContainer.visible()
    }

    private fun requestItemCount() = viewModel.onRequestGetItemCount()

    private fun observeItemCount(itemCount: RequestModelItemCount) {
        val itemQuantity = itemCount.itemCount.peekContent().itemQuantity
        when {
            itemQuantity > 0 ->
                activityCallback.onBadgeCountChanged(itemQuantity)
            else -> Timber.d("egee Cart is empty")
        }
    }

    override fun onResume() {
        super.onResume()
        if (isComingFromSettings && !isGalleryDenied) {
            viewModel.onPermissionsViewRequested(checkSelfCameraPermission())
            isComingFromSettings = false
        }
        if (isComingFromSettings && isGalleryDenied) {
            isComingFromSettings = false
            pickLatestFromGallery()
        }
    }

    private fun pickLatestFromGallery() {
        if (!checkSelfStoragePermission()) {
            return
        }

        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )
        val cursor: Cursor? = context?.contentResolver
            ?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
            )

        if (cursor != null)
            if (cursor.moveToFirst()) {
                val imageLocation: String = cursor.getString(1)
                val imageFile = File(imageLocation)
                val imageUri = Uri.parse(Uri.decode(imageLocation))
                if (imageFile.exists()) {
                    if (imageUri != null)
                        imageGalleryButton.setPadding(4, 4, 4, 4)
                    Glide.with(this).load(imageUri.path)
                        .override(100, 100)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageGalleryButton)
                    cursor.close()
                }
            }
    }

    private fun observeUpdateUI(model: UiModel) {
        when (model) {
            is UiModel.CameraInitializeScreen -> {
                viewModel.onPermissionsViewRequested(checkSelfCameraPermission())
            }

            is UiModel.PermissionsViewRequested -> {
                modelUiState = ModelStatus.PERMISSION
                screenNavigator.toCameraPermissionScreen(this)
                setPermissionView()
            }

            is UiModel.RequestCameraViewDisplay -> cameraPermissionRequester.request({ isPermissionGranted ->
                viewModel.onCameraPermissionRequested(isPermissionGranted)
            }, (::observeDenyPermission))

            is UiModel.CameraViewDisplay -> {
                modelUiState = ModelStatus.FEED
                screenNavigator.toCameraFeedScreen(this)
                setCameraSpecs()
            }
        }
    }

    private fun observeCancelRestoreRequest(cancelModelEvent: Event<CancelModel>) {
        cancelModelEvent.getContentIfNotHandled()?.let {
            restoreCameraView()
        }
    }

    private fun observeErrorResponse(modelErrorEvent: Event<DialogModel>) {
        modelUiState = ModelStatus.LOADING
        modelErrorEvent.getContentIfNotHandled()?.let { dialogModel ->
            if (dialogModel is DialogModel.NoProductsAvailable) {
                screenNavigator.toCameraLoading(this)
            } else {
                screenNavigator.toCameraFeedScreen(this)
            }
            when (dialogModel) {
                is DialogModel.TimeOutError -> {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.no_lightbulb_identified_event)) {
                        putString(
                            getString(R.string.error_reason_event),
                            getString(R.string.time_out_event)
                        )
                    }
                    CrashlyticsException(TIME_OUT_LOG_REPORT, null, null).logException()
                    showErrorDialog(
                        getString(R.string.oops),
                        getString(R.string.timeout_sub),
                        getString(R.string.ok),
                        false
                    )
                }

                is DialogModel.NotBulbIdentified -> {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.no_lightbulb_identified_event)) {
                        putString(
                            getString(R.string.error_reason_event),
                            getString(R.string.no_lightbulb_identified_event)
                        )
                    }
                    showNoBulbErrorDialog(
                        getString(R.string.unidentified),
                        getString(R.string.unidentified_sub),
                        getString(R.string.try_again)
                    )
                }

                is DialogModel.ServerError -> {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.no_lightbulb_identified_event)) {
                        putString(
                            getString(R.string.error_reason_event),
                            getString(R.string.api_server_error_event)
                        )
                    }
                    if (dialogModel.exception is ParsingError) {
                        CrashlyticsException(
                            PARSE_ERROR_LOG_REPORT,
                            dialogModel.errorMessage,
                            null
                        ).logException()
                    }
                    showErrorDialog(
                        getString(R.string.oops),
                        getString(R.string.error_sub),
                        getString(R.string.ok),
                        false

                    )
                }

                is DialogModel.NoProductsAvailable -> {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.lightbulb_identified_but_no_products)) {
                        putString(
                            getString(R.string.base),
                            dialogModel.messages[0].baseIdentified
                        )
                        putString(
                            getString(R.string.shape),
                            dialogModel.messages[0].shapeIdentified
                        )
                    }
                    showErrorDialog(
                        String.format(
                            getString(R.string.bulb_not_available),
                            dialogModel.messages[0].baseIdentified,
                            dialogModel.messages[0].shapeIdentified
                        ),
                        String.format(
                            getString(R.string.error_sub_not_available),
                            dialogModel.messages[0].baseIdentified,
                            dialogModel.messages[0].shapeIdentified
                        ),
                        getString(R.string.ok),
                        false

                    )
                }
                is DialogModel.PermissionPermanentlyDenied -> {
                    isGalleryDenied = false
                    showErrorDialog(
                        getString(R.string.enable_camera_access),
                        getString(R.string.enable_subtitle),
                        getString(R.string.enable_camera_button),
                        true
                    )

                }

                is DialogModel.GalleryPermissionPermanentlyDenied -> {
                    isGalleryDenied = true
                    showErrorDialog(
                        getString(R.string.enable_gallery_access),
                        getString(R.string.enable_gallery_subtitle),
                        getString(R.string.enable_camera_button),
                        true
                    )
                }

            }
        }
    }

    private fun observeDialogButtonAction(modelDialogEvent: Event<ResponseDialogModel>) {
        modelDialogEvent.getContentIfNotHandled()?.let { dialogModel ->
            when (dialogModel) {
                is ResponseDialogModel.PositiveButton -> {
                    when (dialogModel.message) {
                        "retry" -> revertCameraView()
                        "enable" -> deepLinkToSettings()
                    }

                    alertDialog.dismiss()
                }

                is ResponseDialogModel.SecondaryButton -> {//TODO
                }
            }
        }
    }

    private fun observeModelContent(modelContent: Content) {
        when (modelContent) {
            is Content.EncodeImage -> imageRepository.convertImageToBase64(
                modelContent.bitmap,
                ::handleResultBase64
            )
            is Content.RequestCategoriesMessages -> {
                viewModel.onRequestCategoriesMessages(modelContent.encodedImage)
            }
            is Content.RequestModelContent -> {
                navigateToCategories(modelContent.messages)
            }
        }
    }


    private fun observePreviewView(previewModel: Event<PreviewModel>) {
        previewModel.getContentIfNotHandled()?.let {
            layoutCamera.gone()
            layoutPermission.gone()
            browseButton.gone()
            cameraUiContainer.gone()//TODO change the order of this visibility
            layoutPreview.visible()
            imageViewPreview.loadImage(it.bitmap, it.rotationDegrees)
            //start countdown
            timer.start()
            modelUiState = ModelStatus.LOADING
            screenNavigator.toCameraLoading(this)
            cancelButton.setOnClickListener {
                viewModel.onCancelRequest()
                firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.cancel_identified_event)) {}
            }
        }
    }

    fun getStatusView(): ModelStatus = modelUiState

    private fun observeFlashButtonAction(flashModel: FlashModel) {
        flashMode = when (flashModel) {
            is FlashModel.ModeOn -> {
                flashSwitchButton.setImageResource(R.drawable.ic_flash_on)
                ImageCapture.FLASH_MODE_ON
            }
            is FlashModel.ModeOff -> {
                flashSwitchButton.setImageResource(R.drawable.ic_flash_off)
                ImageCapture.FLASH_MODE_OFF
            }
        }
        imageCapture?.flashMode = flashMode

    }

    private fun observeDenyPermission(isPermanentlyDenied: Boolean) {
        viewModel.onPermissionDenied(isPermanentlyDenied, false)
    }

    private fun observeGalleryDenyPermission(isPermanentlyDenied: Boolean) {
        viewModel.onPermissionDenied(isPermanentlyDenied, true)
    }

    private fun observeNavigationFiltering(nav: Event<NavigationToBrowsingFiltering>) {
        nav.getContentIfNotHandled()?.let {
            screenNavigator.navigateToBrowsingFiltering()
        }
    }

    private fun handleResultBase64(base64: String) {
        viewModel.onRequestCategoriesMessages(base64)
    }

    private fun revertCameraView() {
        layoutPreview.gone()
        browseButton.visible()
        layoutCamera.visible()
        cameraUiContainer.visible()
        displayCameraItemsControl()

        lottieAnimationView.playAnimation() //restore lottie view again after being consumed
        initializeLottieAnimation()
    }

    private fun deepLinkToSettings() {
        isComingFromSettings = true
        startActivity(Intent(Settings.ACTION_SETTINGS))
    }


    private fun setPermissionView() {
        layoutCamera.gone()
        browseButton.visible()
        layoutPermission.visible()
        enableContainer.setOnClickListener {
            viewModel.onRequestCameraViewDisplay()
        }
    }

    private fun navigateToCategories(content: Event<List<Message>>) {
        content.getContentIfNotHandled()?.let { messages ->
            timer.cancel()
            firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.lightbulb_identified_event)) {
                putString(getString(R.string.base), messages[0].categories[0].categoryProductBase)
                putString(getString(R.string.shape), messages[0].categories[0].categoryShape)
            }
            // we set status of type FEED so next time we access this page it will have the FEED value assigned by default
            modelUiState = ModelStatus.FEED
            screenNavigator.navigateToCategoriesScreen(messages[0])
        }
    }


    //TODO make this Error Dialog Generic to the App
    private fun showErrorDialog(
        titleDialog: String,
        subtitleDialog: String,
        buttonPositiveText: String,
        neutralButton: Boolean = true
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.layout_reusable_dialog, null)
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.window?.setDimAmount(0.6f)
        alertDialog.setOnDismissListener {
            //lottie reset after closing dialog
            lottieAnimationView?.playAnimation()
            initializeLottieAnimation()
        }

        timer.cancel()
        lottieAnimationView.pauseAnimation()
        dialogView.buttonPositive.text = buttonPositiveText
        dialogView.textViewTitleDialog.text = titleDialog
        dialogView.textViewSubTitleDialog.text = subtitleDialog
        dialogView.buttonPositive.setOnClickListener {
            if (neutralButton) {
                viewModel.onPositiveAlertDialogButtonClicked("enable")
            } else {
                viewModel.onPositiveAlertDialogButtonClicked("retry")
            }

        }

        dialogView.buttonNegative.gone()
        if (neutralButton) {
            dialogView.buttonNeutral.text = getString(R.string.not_now)
            dialogView.buttonNeutral.setOnClickListener {
                alertDialog.dismiss()
            }
        } else {
            dialogView.buttonNeutral.gone()
        }
        alertDialog.show()

    }


    private fun showNoBulbErrorDialog(
        titleDialog: String,
        subtitleDialog: String,
        buttonPositiveText: String
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.layout_reusable_dialog, null)
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.window?.setDimAmount(0.6f)
        timer.cancel()
        lottieAnimationView.pauseAnimation()
        dialogView.buttonPositive.text = buttonPositiveText
        dialogView.textViewTitleDialog.text = titleDialog
        dialogView.textViewSubTitleDialog.text = subtitleDialog
        dialogView.buttonPositive.setOnClickListener {
            viewModel.onPositiveAlertDialogButtonClicked("retry")
        }

        dialogView.buttonNegative.gone()

        dialogView.buttonNeutral.text = getString(R.string.help_me_scan)
        dialogView.buttonNeutral.setOnClickListener {
            alertDialog.dismiss()
            revertCameraView()
            screenNavigator.navigateToTipsAndTricksScreen()
        }

        alertDialog.show()

    }


    private fun setCameraSpecs() {
        layoutCamera.visible()
        browseButton.visible()
        layoutPermission.gone()

        //outputDirectory = CameraLightFinderActivity.getOutputDirectory(requireContext())

        viewFinder.post {

            displayId = viewFinder.display.displayId


            initCameraUi()

            setUpCamera()

            flashSwitchButton.setOnClickListener {
                viewModel.onFlashModeButtonClicked(flashMode)
            }

            imageGalleryButton.setOnClickListener {
                galleryPermissionRequester.request({ isPermissionGranted ->
                    viewModel.onGalleryPermissionRequested(isPermissionGranted)
                }, (::observeGalleryDenyPermission))
            }

            pickLatestFromGallery()

            /*//TODO check this and move it to local data source
            lifecycleScope.launch(Dispatchers.IO) {
                outputDirectory.listFiles { file ->
                    EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
                }
            }*/
        }
    }

    fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }

        startActivityForResult(intent, REQUEST_IMAGE_GET)

    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCases() {
        /**
         * CAMERA USE-CASES
         */
        val rotation = viewFinder.display.rotation

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()


        preview = Preview.Builder()
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetResolution(Size(600, 800))
            .setFlashMode(flashMode)
            .setTargetRotation(rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor,
                    LuminosityAnalyzer { luma ->
                        Timber.d("$TAG Average luminosity: $luma")
                    })
            }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalyzer
            )

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(viewFinder.createSurfaceProvider(camera?.cameraInfo))
        } catch (exc: Exception) {
            Timber.e("$TAG Use case binding failed $exc")
        }

        /**
         *END USE-CASES
         */
    }

    private fun onCameraCaptureClick() {
        imageCapture?.let { imageCapture ->
            //val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
            val metadata = ImageCapture.Metadata().apply {

                isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
            }
            imageCapture.takePicture(cameraExecutor, imageCaptureListener)

            container.postDelayed({
                container.foreground = ColorDrawable(Color.WHITE)
                container.postDelayed(
                    { container.foreground = null }, ANIMATION_FAST_MILLIS
                )
            }, ANIMATION_SLOW_MILLIS)
        }
    }

    private fun initCameraUi() {

        container.findViewById<ConstraintLayout>(R.id.cameraUiContainer)?.let {
            container.removeView(it)
        }

        controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)

        controls?.cameraCaptureButton?.setSafeOnClickListener(::checkFlagOnView) {
            connectivityRequester.checkConnection { isConnected ->
                if (isConnected) {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.send_photo)) {
                        putBoolean(
                            getString(R.string.flash_enabled),
                            flashMode == ImageCapture.FLASH_MODE_ON
                        )
                    }
                    onCameraCaptureClick()
                } else {
                    displayCameraItemsControl()
                    activityCallback.onInternetConnectionLost()
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.no_lightbulb_identified_event)) {
                        putString(
                            getString(R.string.error_reason_event),
                            getString(R.string.no_internet_connection_event)
                        )
                    }
                }
            }
        }

        helpButton?.setSafeOnClickListener(onSafeClick = {
            firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.photo_help)) {}
            screenNavigator.navigateToTipsAndTricksScreen()
        })

        /**
         * once camera container is initialize we start observing camera events from camera viewmodels
         */
        viewModel.modelFlash.observe(viewLifecycleOwner, Observer(::observeFlashButtonAction))
    }

    private fun checkFlagOnView(flag: Boolean) {
        if (flag) {
            activityCallback.setBottomBarInvisibility(true)
            controls?.cameraCaptureButton?.isEnabled = false
        }
    }

    private fun displayCameraItemsControl() {
        enableCameraCaptureButton()
        activityCallback.setBottomBarInvisibility(false)
    }

    //TODO set this method for extension when media user is implemented
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension
        )

    private fun initializeLottieAnimation() {
        lottieAnimationView?.progress = 0.0f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Shut down our background executor
        cameraExecutor.shutdown()
        // Every time the orientation of device changes, update rotation for use cases
        displayManager.registerDisplayListener(displayListener, null)
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
        }
    }

    fun disableCameraCaptureButton() {
        controls?.cameraCaptureButton?.isEnabled = false

    }

    fun enableCameraCaptureButton() {
        controls?.cameraCaptureButton?.isEnabled = true
    }

    fun restoreCamera() {
        //TODO(this will be implemented differently according to US-1523)
        if (modelUiState != ModelStatus.PERMISSION) {
            viewModel.onRestoreCameraView()
        }
    }

    private fun restoreCameraView() {
        timer.cancel()
        layoutPreview.gone()
        layoutCamera.visible()
        browseButton.visible()
        setBrowsingClickable()
        cameraUiContainer.visible()
        displayCameraItemsControl()
        initializeLottieAnimation()
        modelUiState = ModelStatus.FEED
    }
}

enum class ModelStatus { FEED, LOADING, PERMISSION, GALLERY }