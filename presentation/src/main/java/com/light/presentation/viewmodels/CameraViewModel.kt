package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Message
import com.light.presentation.common.Event
import com.light.usecases.GetCategoriesResultUseCase
import com.light.usecases.GetFilePathImageEncodedUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class CameraViewModel(
    private val getCategoryResultUseCase: GetCategoriesResultUseCase,
    private val getFilePathImageUseCase: GetFilePathImageEncodedUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    companion object {
        const val MODE_ON = 1
        const val MODE_OFF = 2
    }

    private lateinit var dataMessages: List<Message>
    private var flag: STATUS_REQUEST_LOADER = STATUS_REQUEST_LOADER.INITIAL_STATE


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
        object CameraViewPermissionDenied : UiModel()
    }


    private val _modelPreview = MutableLiveData<Event<PreviewModel>>()
    val modelPreview: LiveData<Event<PreviewModel>>
        get() = _modelPreview

    class PreviewModel


    private val _modelRequest = MutableLiveData<Content>()
    val modelRequest: LiveData<Content>
        get() = _modelRequest


    sealed class Content {
        class EncodeImage(val absolutePath: String) : Content()
        class RequestModelContent(val messages: Event<List<Message>>) : Content()
        class RequestCategoriesMessages(val encodedImage: String) : Content()
    }


    private val _modelRequestCancel = MutableLiveData<Event<CancelModel>>()
    val modelRequestCancel: LiveData<Event<CancelModel>>
        get() = _modelRequestCancel

    class CancelModel


    private val _modelError = MutableLiveData<Event<ErrorModel>>()
    val modelError: LiveData<Event<ErrorModel>>
        get() = _modelError

    sealed class ErrorModel {
        object TimeOutError : ErrorModel()
        object NotBulbRecognised : ErrorModel()
        object GeneralError : ErrorModel()

    }

    private val _modelDialog = MutableLiveData<Event<DialogModel>>()
    val modelDialog: LiveData<Event<DialogModel>>
        get() = _modelDialog

    sealed class DialogModel {
        class PositiveButton(val message: String) : DialogModel()
        class SecondaryButton(val message: String) : DialogModel()
    }

    private val _modelFlash = MutableLiveData<FlashModel>()
    val modelFlash: LiveData<FlashModel>
        get() = _modelFlash

    sealed class FlashModel(val mode: Int) {
        object ModeOn : FlashModel(MODE_OFF)
        object ModeOff : FlashModel(MODE_ON)
    }

    fun onSendButtonClicked(absolutePath: String) {
        _modelPreview.value = Event(PreviewModel())
        _modelRequest.value = Content.EncodeImage(absolutePath)
    }

    fun onRequestCategoriesMessages(base64: String) {
        checkCoroutineIsCancelled()
        launch {
            getCategoryResultUseCase.execute(
                ::handleSuccessResponse,
                ::handleErrorResponse,
                ::handleCancelResponse,
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

        } else {
            _model.value = UiModel.CameraViewPermissionDenied
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

    fun onCheckLoaderAnimationConsumed() {
        when (flag) {
            STATUS_REQUEST_LOADER.INITIAL_STATE -> {
                flag = STATUS_REQUEST_LOADER.ANIMATION_CONSUMED
                cancelRequestScope()
            }

            STATUS_REQUEST_LOADER.DATA_RETRIEVED -> {
                flag = STATUS_REQUEST_LOADER.INITIAL_STATE
                _modelRequest.value = Content.RequestModelContent(Event(dataMessages))
            }

            STATUS_REQUEST_LOADER.ERROR -> {
                flag = STATUS_REQUEST_LOADER.INITIAL_STATE
            }
        }
    }

    fun onRequestFileImageEncoded(absolutePath: String) {
        checkCoroutineIsCancelled()
        launch {
            getFilePathImageUseCase.execute(
                ::handleFileImageRetrieved, params = *arrayOf(absolutePath)
            )
        }
    }

    fun onCancelRequest() {
        if (flag == STATUS_REQUEST_LOADER.DATA_RETRIEVED) {
            flag = STATUS_REQUEST_LOADER.INITIAL_STATE
        } else {
            cancelRequestScope()
        }
        _modelRequestCancel.value = Event(CancelModel())
    }

    fun onPositiveAlertDialogButtonClicked() {
        _modelDialog.value = Event(DialogModel.PositiveButton(""))
    }

    fun onFlashModeButtonClicked(flashMode: Int) {
        if (flashMode == MODE_ON) {
            _modelFlash.value = FlashModel.ModeOff
        } else if (flashMode == MODE_OFF) {
            _modelFlash.value = FlashModel.ModeOn
        }
    }

    private fun handleSuccessResponse(messages: List<Message>) {
        flag = STATUS_REQUEST_LOADER.DATA_RETRIEVED
        //TODO the logic here!!
        dataMessages = messages
    }

    private fun handleErrorResponse(throwable: Throwable) {
        flag = STATUS_REQUEST_LOADER.INITIAL_STATE
        _modelError.value = Event(ErrorModel.GeneralError)
    }

    private fun handleEmptyResponse() {
        flag = STATUS_REQUEST_LOADER.INITIAL_STATE
        _modelError.value = Event(ErrorModel.NotBulbRecognised)
    }

    private fun handleFileImageRetrieved(imageEncoded: String) {
        _modelRequest.value = Content.RequestCategoriesMessages(imageEncoded)
    }

    private fun handleCancelResponse(message: String) {
        if (flag == STATUS_REQUEST_LOADER.ANIMATION_CONSUMED) {
            flag = STATUS_REQUEST_LOADER.INITIAL_STATE
            _modelError.value = Event(ErrorModel.TimeOutError)
        }
    }


}

enum class STATUS_REQUEST_LOADER { INITIAL_STATE, DATA_RETRIEVED, ANIMATION_CONSUMED, ERROR }