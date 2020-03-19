package com.light.presentation.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Message
import com.light.presentation.common.Event
import com.light.usecases.GetCategoriesResultUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class CameraViewModel(
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


    private val _modelPreview = MutableLiveData<Event<PreviewModel>>()
    val modelPreview: LiveData<Event<PreviewModel>>
        get() = _modelPreview

    class PreviewModel(val bitmap: Bitmap)


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
        object ServerError : DialogModel()
        class PermissionPermanentlyDenied(val isPermanentlyDenied: Boolean) : DialogModel()

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

    fun onCameraButtonClicked(bitmap: Bitmap) {
        _modelPreview.value = Event(PreviewModel(bitmap))
        _modelRequest.value = Content.EncodeImage(bitmap)
    }

    fun onRequestCategoriesMessages(base64: String) {
        checkCoroutineIsCancelled()
        launch {
            getCategoryResultUseCase.execute(
                ::handleSuccessResponse,
                ::handleErrorResponse,
                ::handleTimeOutResponse,
                ::handleEmptyResponse,
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

    fun onPermissionDenied(isPermanentlyDenied: Boolean) {
        if(isPermanentlyDenied){
            _modelDialog.value = Event(DialogModel.PermissionPermanentlyDenied(isPermanentlyDenied))
        }
    }

    private fun handleSuccessResponse(messages: List<Message>) {
        _modelRequest.value = Content.RequestModelContent(Event(messages))
    }

    private fun handleErrorResponse(hasBeenCanceled: Boolean) {
        if (!hasBeenCanceled) {
            Log.d("Gabriel", "handle Error Response!!")
            _modelDialog.value = Event(DialogModel.ServerError)

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

