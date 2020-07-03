package com.light.presentation.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.CartItemCount
import com.light.domain.model.Message
import com.light.presentation.common.Event
import com.light.usecases.GetCategoriesResultUseCase
import com.light.usecases.GetItemCountUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class CameraViewModel(
    private val getItemCount: GetItemCountUseCase,
    private val getCategoryResultUseCase: GetCategoriesResultUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    companion object {
        const val MODE_ON = 1
        const val MODE_OFF = 2
    }


    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) init()
            return _model
        }

    sealed class UiModel {
        object CameraInitializeScreen : UiModel()
        object RequestCameraViewDisplay : UiModel()
        object PermissionsViewRequested : UiModel()
        object CameraViewDisplay : UiModel()
    }

    class GalleryViewDisplay

    private val _modelGallery = MutableLiveData<Event<GalleryViewDisplay>>()
    val modelGallery: LiveData<Event<GalleryViewDisplay>>
        get() = _modelGallery


    private val _modelPreview = MutableLiveData<Event<PreviewModel>>()
    val modelPreview: LiveData<Event<PreviewModel>>
        get() = _modelPreview

    class PreviewModel(val bitmap: Bitmap, val rotationDegrees: Int)


    private val _modelRequest = MutableLiveData<Content>()
    val modelRequest: LiveData<Content>
        get() = _modelRequest


    sealed class Content {
        class EncodeImage(val bitmap: Bitmap) : Content()
        class RequestModelContent(val messages: Event<List<Message>>) : Content()
        class RequestCategoriesMessages(val encodedImage: String) : Content()
    }


    private val _modelRequestCancel = MutableLiveData<Event<CancelModel>>()
    val modelRequestCancel: LiveData<Event<CancelModel>>
        get() = _modelRequestCancel

    class CancelModel


    private val _modelDialog = MutableLiveData<Event<DialogModel>>()
    val modelDialog: LiveData<Event<DialogModel>>
        get() = _modelDialog

    sealed class DialogModel {
        object TimeOutError : DialogModel()
        object NotBulbIdentified : DialogModel()
        data class NoProductsAvailable(val messages: List<Message>) : DialogModel()
        data class ServerError(val exception: Exception? = null, val errorMessage: String) :
            DialogModel()

        data class PermissionPermanentlyDenied(val isPermanentlyDenied: Boolean) : DialogModel()
        data class GalleryPermissionPermanentlyDenied(val isPermanentlyDenied: Boolean) : DialogModel()

    }

    private val _modelResponseDialog = MutableLiveData<Event<ResponseDialogModel>>()
    val modelResponseDialog: LiveData<Event<ResponseDialogModel>>
        get() = _modelResponseDialog

    sealed class ResponseDialogModel {
        class PositiveButton(val message: String) : ResponseDialogModel()
        class SecondaryButton(val message: String) : ResponseDialogModel()
    }

    private val _modelFlash = MutableLiveData<FlashModel>()
    val modelFlash: LiveData<FlashModel>
        get() = _modelFlash

    sealed class FlashModel {
        object ModeOn : FlashModel()
        object ModeOff : FlashModel()
    }

    private val _modelItemCountRequest = MutableLiveData<RequestModelItemCount>()
    val modelItemCountRequest: LiveData<RequestModelItemCount>
        get() = _modelItemCountRequest


    data class RequestModelItemCount(val itemCount: Event<CartItemCount>)


    fun onRequestGetItemCount() {
        launch {
            getItemCount.execute(
                ::handleItemCountSuccessResponse
            )
        }
    }

    private fun handleItemCountSuccessResponse(cartItemCount: CartItemCount) {
        _modelItemCountRequest.value = RequestModelItemCount(Event(cartItemCount))
    }

    fun onCameraButtonClicked(bitmap: Bitmap, rotationDegrees: Int) {
        _modelPreview.postValue(Event(PreviewModel(bitmap, rotationDegrees)))
        _modelRequest.postValue(Content.EncodeImage(bitmap))
    }

    fun onRequestCategoriesMessages(base64: String) {
        launch {
            getCategoryResultUseCase.execute(
                ::handleSuccessResponse,
                ::handleErrorResponse,
                ::handleTimeOutResponse,
                ::handleEmptyResponse,
                ::handleNoProductsResponse,
                base64
            )
        }
    }

    private fun init() {
        _model.value = UiModel.CameraInitializeScreen
    }

    fun onCameraPermissionRequested(isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            _model.value = UiModel.CameraViewDisplay

        }
    }

    fun onGalleryPermissionRequested(isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            _modelGallery.value = Event(GalleryViewDisplay())
        }
    }

    fun onRequestCameraViewDisplay() {
        _model.value = UiModel.RequestCameraViewDisplay
    }

    fun onPermissionsViewRequested(isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            _model.value = UiModel.CameraViewDisplay

        } else {
            _model.value = UiModel.PermissionsViewRequested
        }
    }

    fun onCancelRequest() {
        cancelRequestScope()
    }

    fun onPositiveAlertDialogButtonClicked(message: String) {
        _modelResponseDialog.value = Event(ResponseDialogModel.PositiveButton(message))
    }

    fun onFlashModeButtonClicked(flashMode: Int) {
        if (flashMode == MODE_ON) {
            _modelFlash.value = FlashModel.ModeOff
        } else if (flashMode == MODE_OFF) {
            _modelFlash.value = FlashModel.ModeOn
        }
    }

    fun onPermissionDenied(isPermanentlyDenied: Boolean, isGalleryPermission: Boolean) {
        if (isPermanentlyDenied && !isGalleryPermission) {
            _modelDialog.value = Event(DialogModel.PermissionPermanentlyDenied(isPermanentlyDenied))
        } else if (isPermanentlyDenied && isGalleryPermission) {
            _modelDialog.value = Event(DialogModel.GalleryPermissionPermanentlyDenied(isPermanentlyDenied))
        }
    }

    private fun handleSuccessResponse(messages: List<Message>) {
        _modelRequest.value = Content.RequestModelContent(Event(messages))
    }

    private fun handleNoProductsResponse(messages: List<Message>) {
        _modelDialog.value = Event(DialogModel.NoProductsAvailable(messages))
    }

    private fun handleErrorResponse(
        hasBeenCanceled: Boolean,
        errorException: Exception?,
        messageError: String
    ) {
        if (!hasBeenCanceled) {
            _modelDialog.value = Event(
                DialogModel.ServerError(
                    exception = errorException,
                    errorMessage = messageError
                )
            )

        } else {
            _modelRequestCancel.value = Event(CancelModel())

        }
    }

    private fun handleEmptyResponse() {
        _modelDialog.value = Event(DialogModel.NotBulbIdentified)
    }

    //TODO it might be used on media image user story
    private fun handleFileImageRetrieved(imageEncoded: String) {
        _modelRequest.value = Content.RequestCategoriesMessages(imageEncoded)
    }

    private fun handleTimeOutResponse(message: String) {
        _modelDialog.value = Event(DialogModel.TimeOutError)
    }

}

