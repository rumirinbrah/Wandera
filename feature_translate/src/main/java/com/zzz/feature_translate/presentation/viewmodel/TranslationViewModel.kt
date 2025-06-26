package com.zzz.feature_translate.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.domain.network.NetworkObserver
import com.zzz.core.domain.network.NetworkSpecs
import com.zzz.core.domain.network.NetworkType
import com.zzz.core.platform.notification.WanderaNotification
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.translate.model.TranslationModel
import com.zzz.data.translate.source.TranslateSource
import com.zzz.feature_translate.data.local.TranslatePreferences
import com.zzz.feature_translate.manager.TranslationManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslationViewModel(
    private val dataSource: TranslateSource ,
    private val translationManager: TranslationManager ,
    private val translatePreferences: TranslatePreferences ,
    private val wanderaNotification: WanderaNotification ,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    private val _models = MutableStateFlow<List<TranslationModel>>(emptyList())
    val models = _models.asStateFlow()

    private val _state = MutableStateFlow(TranslateState())
    val state = _state.asStateFlow()

    private var internalState = InternalState()

    private var _networkState : NetworkSpecs = NetworkSpecs()

    //we use this to make sure we dont initialize translator multiple times for same ip op lang
    private var sessionOngoing: Boolean = false


    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    init {
        Log.d("translateVm" , "TranslationViewModel init...")
        _state.update {
            it.copy(
                isFirstTime = translatePreferences.getFirstTime()
            )
        }
        collectModels()
        viewModelScope.launch {
            networkObserver.isConnected
                .collect{
                    _networkState = it
                }

        }
        //validateDownloadedModels()
    }

    fun onAction(action: TranslateAction) {
        when (action) {
            is TranslateAction.TranslatorAction -> {
                when (action) {
                    TranslateAction.TranslatorAction.ClearResources -> TODO()
                    is TranslateAction.TranslatorAction.CreateTranslator -> TODO()
                    is TranslateAction.TranslatorAction.TranslateText -> {
                        translate()
                    }

                    is TranslateAction.TranslatorAction.ChangeDestLanguage -> {
                        changeDestLang(action.name , action.modelCode)
                    }

                    is TranslateAction.TranslatorAction.ChangeSrcLanguage -> {
                        changeSrcLang(action.name , action.modelCode)
                    }
                }
            }

            is TranslateAction.ManagerAction -> {
                when (action) {
                    is TranslateAction.ManagerAction.SetModelToDelete -> {
                        setModelToDelete(action.modelCode , action.name)
                    }

                    TranslateAction.ManagerAction.DeleteModel -> {
                        deleteModel()
                    }

                    is TranslateAction.ManagerAction.DownloadModel -> {
                        downloadModel(action.modelCode , action.name)
                    }
                    TranslateAction.ManagerAction.DownloadModelWithCellularData->{
                        downloadModelWithCellularData()
                    }

                    is TranslateAction.ManagerAction.FilterByDownloads -> {
                        //setDownloadFilter(action.filter)
                    }

                    TranslateAction.ManagerAction.DismissCellularDownloadDialog -> {

                    }
                }
            }

            is TranslateAction.OnTextChange -> {
                onTextChange(action.value)
            }

            TranslateAction.OnClearText -> {
                onClearText()
            }
        }
    }


    private fun collectModels() {
        viewModelScope.launch {
            _state.update {
                it.copy(loading = true)
            }
            dataSource.getModels()
                .onStart {
                    _state.update {
                        it.copy(loading = false)
                    }
                }
                .flowOn(Dispatchers.IO)
                .onCompletion { error ->
                    if (error is CancellationException) {
                        throw error
                    }
                }
                .collect { newModels ->
                    _models.update {
                        newModels
                    }
                }
        }
    }

    private fun validateDownloadedModels() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    loading = true
                )
            }
            try {
                val modelCodes = translationManager.getDownloadedModels()
                modelCodes.onEach { modelCode ->
                    launch {

                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    //---------- TRANSLATOR -----------

    private fun translate() {
        viewModelScope.launch {
            if (!validateTranslatorInput()) {
                _events.send(UIEvents.Error("Please select valid translate methods!"))
                return@launch
            }
            _state.update {
                it.copy(translating = true)
            }
            createTranslator()
            try {
                val result = translationManager.translateText(_state.value.sourceText)
                _state.update {
                    it.copy(
                        translating = false ,
                        translatedText = result
                    )
                }
                println(result)
            } catch (e: Exception) {
                _state.update {
                    it.copy(translating = false)
                }
                _events.send(
                    UIEvents.Error(
                        e.message ?: "An unknown error occurred while translating"
                    )
                )
            }

        }
    }

    //translator init
    private fun createTranslator() {
        if (!sessionOngoing) {
            translationManager.createTranslator(
                _state.value.srcLanguageCode!! ,
                _state.value.destLanguageCode!!
            )
            sessionOngoing = true
        }
    }

    private fun validateTranslatorInput(): Boolean {
        val value = _state.value
        if (value.srcLanguageCode == null || value.destLanguageCode == null) {
            return false
        }
        return true
    }

    //change dest
    private fun changeDestLang(name: String , code: String) {
        viewModelScope.launch {
            sessionOngoing = false

            _state.update {
                it.copy(
                    destLanguage = name ,
                    destLanguageCode = code
                )
            }
        }
    }

    //change ip
    private fun changeSrcLang(name: String , code: String) {
        viewModelScope.launch {
            sessionOngoing = false

            _state.update {
                it.copy(
                    srcLanguage = name ,
                    srcLanguageCode = code
                )
            }
        }
    }

    // ---text field----
    private fun onTextChange(value: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(sourceText = value)
            }
        }
    }

    private fun onClearText() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    sourceText = "" ,
                    translatedText = ""
                )
            }
        }
    }


    //clears input methods, src, translated text
    private fun resetTranslateTabState() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    srcLanguage = "" ,
                    srcLanguageCode = null ,
                    destLanguage = "" ,
                    destLanguageCode = null ,
                    translatedText = "" ,
                    sourceText = ""
                )
            }
        }
    }

    //---------- MANAGER -----------
    private fun resolveDownload(modelCode: String , name: String){
        viewModelScope.launch {
            if (_state.value.downloading) {
                _events.send(UIEvents.Error("Another model is being downloaded, please wait..."))
                return@launch
            }
            when(_networkState.type) {
                NetworkType.UNKNOWN -> {
                    _events.send(UIEvents.Error("Seems your network is unstable, please try later..."))
                    return@launch
                }
                NetworkType.MOBILE_DATA -> {
                    _state.update {
                        it.copy(cellularDataDownloadDialog = true)
                    }
                    internalState = internalState.copy(
                        modelName = name ,
                        modelCode = modelCode
                    )
                    return@launch
                }
                else -> {
                    downloadModel(modelCode , name)
                }
            }
        }
    }
    private fun downloadModel(modelCode: String , name: String, wifiRequired : Boolean = true) {
        viewModelScope.launch {
            if (_state.value.downloading) {
                _events.send(UIEvents.Error("Another model is being downloaded, please wait..."))
                return@launch
            }
            when(_networkState.type) {
                NetworkType.UNKNOWN -> {
                    _events.send(UIEvents.Error("Seems your network is unstable, please try later..."))
                    return@launch
                }
                NetworkType.MOBILE_DATA -> {
                    _state.update {
                        it.copy(cellularDataDownloadDialog = true)
                    }
                    internalState = internalState.copy(
                        modelName = name ,
                        modelCode = modelCode
                    )
                    return@launch
                }
                else -> Unit
            }


            _state.update {
                it.copy(downloading = true)
            }
            wanderaNotification.sendDownloadNotification(name)
            _events.send(UIEvents.SuccessWithMsg("$name model is being downloaded, feel free to switch to other apps in the meantime!"))
            try {

                translationManager.downloadModel(languageCode = modelCode, wifiRequired = wifiRequired)

                //set model true in db
                dataSource.updateModelStatus(modelCode , true)


                _state.update {
                    it.copy(downloading = false)
                }

                wanderaNotification.cancelDownloadingNotification()
                wanderaNotification.sendDownloadFinishedNotification(name)

                _events.send(UIEvents.SuccessWithMsg("Model has been downloaded successfully!"))

            } catch (e: Exception) {
                //errur
                _state.update {
                    it.copy(downloading = false)
                }
                wanderaNotification.cancelAll()
                _events.send(
                    UIEvents.Error(
                        e.message ?: "An unknown error occurred downloading model"
                    )
                )
            }
        }
    }
    private fun downloadModelWithCellularData(){
        viewModelScope.launch {
            _state.update {
                it.copy(cellularDataDownloadDialog = false)
            }
            if(internalState.modelCode == null || internalState.modelName == null){
                return@launch
            }
            if (_state.value.downloading) {
                _events.send(UIEvents.Error("Another model is being downloaded, please wait..."))
                return@launch
            }

            _state.update {
                it.copy(downloading = true)
            }
            wanderaNotification.sendDownloadNotification(internalState.modelName!!)
            _events.send(UIEvents.SuccessWithMsg("${internalState.modelName} model is being downloaded, feel free to switch to other apps in the meantime!"))
            try {

                translationManager.downloadModel(internalState.modelCode!!,false)

                //set model true in db
                dataSource.updateModelStatus(internalState.modelCode!! , true)


                _state.update {
                    it.copy(downloading = false)
                }

                wanderaNotification.cancelDownloadingNotification()
                wanderaNotification.sendDownloadFinishedNotification(internalState.modelName!!)

                resetInternalState()
                _events.send(UIEvents.SuccessWithMsg("Model has been downloaded successfully!"))

            } catch (e: Exception) {
                //errur
                _state.update {
                    it.copy(downloading = false)
                }
                wanderaNotification.cancelAll()
                resetInternalState()
                _events.send(
                    UIEvents.Error(
                        e.message ?: "An unknown error occurred downloading model"
                    )
                )
            }
        }
    }

    private fun dismissCellularDownloadDialog(){
        viewModelScope.launch {
            _state.update {
                it.copy(cellularDataDownloadDialog = false)
            }
            resetInternalState()
        }
    }
    private fun resetInternalState(){
        internalState = InternalState()
    }

    private fun setModelToDelete(modelCode: String? , name: String?) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    modelToDeleteCode = modelCode ,
                    modelToDelete = name
                )
            }
        }
    }

    private fun deleteModel() {
        if (_state.value.deleting) {
            return
        }
        viewModelScope.launch {
            if (_state.value.modelToDelete == null || _state.value.modelToDeleteCode == null) {
                _events.send(UIEvents.Error("Failed to delete, No model selected!"))
                return@launch
            }
            try {
                _state.update {
                    it.copy(deleting = true)
                }
                val modelCode = _state.value.modelToDeleteCode!!
                translationManager.deleteModel(modelCode)

                //set model false in db
                dataSource.updateModelStatus(modelCode , false)


                _state.update {
                    it.copy(
                        deleting = false ,
                        modelToDelete = null ,
                        modelToDeleteCode = null
                    )
                }
                resetTranslateTabState()
                _events.send(UIEvents.SuccessWithMsg("Model has been deleted successfully!"))
            } catch (e: Exception) {
                //errur
                _state.update {
                    it.copy(
                        deleting = false ,
                        modelToDelete = null ,
                        modelToDeleteCode = null
                    )
                }
                _events.send(
                    UIEvents.Error(
                        e.message ?: "An unknown error occurred deleting model"
                    )
                )
            }
        }
    }

    private fun setDownloadFilter(filter: Boolean) {
        viewModelScope.launch {
            if (filter) {
                val downloads = _models.value.filter { it.downloaded }
                _state.update {
                    it.copy(
                        downloadedModels = downloads ,
                        downloadsFilter = true
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        downloadedModels = null ,
                        downloadsFilter = false
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        translationManager.clearTranslator()
    }


}