package com.light.presentation.viewmodels

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

    private lateinit var dataMessages: List<Message>
    private var flag: STATUS_REQUEST_LOADER = STATUS_REQUEST_LOADER.NO_RESPONSE_YET


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
    }


    private val _modelRequestCancel = MutableLiveData<Event<CancelModel>>()
    val modelRequestCancel: LiveData<Event<CancelModel>>
        get() = _modelRequestCancel

    class CancelModel


    private val _modelError = MutableLiveData<Event<ErrorModel>>()
    val modelError: LiveData<Event<ErrorModel>>
        get() = _modelError

    class ErrorModel(val throwable: Throwable? = null, val isTimeout: Boolean = false)


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
            STATUS_REQUEST_LOADER.DATA_RETRIEVED -> {
                flag = STATUS_REQUEST_LOADER.NO_RESPONSE_YET
                _modelRequest.value = Content.RequestModelContent(Event(dataMessages))
            }
            STATUS_REQUEST_LOADER.NO_RESPONSE_YET -> {
                /**
                 *(the lottie time animation has been consumed(finish),
                 * so if it satisfies this else, it means we still have no response. Therefore we cancel the request and
                 * cause an error
                 */
                /**
                 *(the lottie time animation has been consumed(finish),
                 * so if it satisfies this else, it means we still have no response. Therefore we cancel the request and
                 * cause an error
                 */
                /**
                 *(the lottie time animation has been consumed(finish),
                 * so if it satisfies this else, it means we still have no response. Therefore we cancel the request and
                 * cause an error
                 */
                flag = STATUS_REQUEST_LOADER.ANIMATION_CONSUMED
                cancelRequestScope()
            }
            STATUS_REQUEST_LOADER.ERROR -> {
                flag = STATUS_REQUEST_LOADER.NO_RESPONSE_YET
            }
        }
    }

    fun onCancelRequest() {
        if (flag == STATUS_REQUEST_LOADER.DATA_RETRIEVED) {
            flag = STATUS_REQUEST_LOADER.NO_RESPONSE_YET
        } else {
            cancelRequestScope()
        }
        _modelRequestCancel.value = Event(CancelModel())
    }

    private fun handleSuccessResponse(messages: List<Message>) {
        flag = STATUS_REQUEST_LOADER.DATA_RETRIEVED
        dataMessages = messages
    }

    private fun handleErrorResponse(throwable: Throwable) {
        flag = STATUS_REQUEST_LOADER.ERROR
        _modelError.value = Event(ErrorModel(throwable))
    }

    private fun handleCancelResponse(message: String) {
        if (flag == STATUS_REQUEST_LOADER.ANIMATION_CONSUMED) {
            flag = STATUS_REQUEST_LOADER.NO_RESPONSE_YET
            _modelError.value = Event(ErrorModel(isTimeout = true))
        }
    }


}

enum class STATUS_REQUEST_LOADER { DATA_RETRIEVED, NO_RESPONSE_YET, ANIMATION_CONSUMED, ERROR }