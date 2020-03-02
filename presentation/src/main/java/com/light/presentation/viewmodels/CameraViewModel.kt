package com.light.presentation.viewmodels

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

    private lateinit var dataMessages: List<Message>
    private var flag: TYPE_RESPONSE = TYPE_RESPONSE.NO_RESPONSE


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
        object Loading : Content()
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

    class ErrorModel(val throwable: Throwable)

    fun onSendButtonClicked(absolutePath: String) {
        _modelPreview.value = Event(PreviewModel())
        _modelRequest.value = Content.EncodeImage(absolutePath)
    }

    fun onRequestCategoriesMessages(base64: String) {
        _modelRequest.value = Content.Loading
        checkCoroutineIsCancelled()
        launch {
            getCategoryResultUseCase.execute(
                ::handleMessagesResponse,
                ::handleErrorResponse,
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

    private fun handleMessagesResponse(messages: List<Message>) {
        flag = TYPE_RESPONSE.SUCCESS
        dataMessages = messages
    }

    private fun handleErrorResponse(throwable: Throwable) {
        Log.d("Gabriel","TIME OUT")
        _modelError.value = Event(ErrorModel(throwable))
    }

    fun onCheckResultRequest() {
        if(flag == TYPE_RESPONSE.SUCCESS){
            _modelRequest.value = Content.RequestModelContent(Event(dataMessages))
            flag = TYPE_RESPONSE.NO_RESPONSE
        }
    }


    fun onCancelRequest() {
        destroyScope()
        _modelRequestCancel.value = Event(CancelModel())
    }


}

enum class TYPE_RESPONSE { SUCCESS, ERROR, NO_RESPONSE }