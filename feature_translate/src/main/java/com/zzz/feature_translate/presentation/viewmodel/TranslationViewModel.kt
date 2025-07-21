package com.zzz.feature_translate.presentation.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslationViewModel(
    private val dataSource: TranslateSource ,
    private val translationManager: TranslationManager ,
    private val translatePreferences: TranslatePreferences ,
    private val wanderaNotification: WanderaNotification ,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    private var loggingEnabled = true

    private val _models = MutableStateFlow<List<TranslationModel>>(emptyList())
//    val models = _models.asStateFlow()

    private val _state = MutableStateFlow(
        TranslateState()
    )
    val state = _state.asStateFlow()

    /*
    val state = combine(
        _state,
        _models
    ){translateState,models->
        println("--COMBINE--")
        if(translateState.downloadsFilter){
            translateState.copy(filteredModels = models.filter { it.downloaded })
        }else{
            translateState.copy(filteredModels = models)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )

     */

    private var internalState = InternalState()

    private var networkSpecs: NetworkSpecs = NetworkSpecs()

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
        observeNetwork()

    }

    fun onAction(action: TranslateAction) {
        when (action) {
            is TranslateAction.TranslatorAction -> {
                when (action) {
                    TranslateAction.TranslatorAction.ClearResources -> TODO()
                    is TranslateAction.TranslatorAction.CreateTranslator -> TODO()
                    is TranslateAction.TranslatorAction.TranslateText -> {
                        resolveTranslate()
                    }

                    is TranslateAction.TranslatorAction.ChangeDestLanguage -> {
                        changeDestLang(action.name , action.modelCode)
                    }
                    is TranslateAction.TranslatorAction.ChangeSrcLanguage -> {
                        changeSrcLang(action.name , action.modelCode)
                    }
                    is TranslateAction.TranslatorAction.SetTextFormatting->{
                        setTextFormatting(action.keepFormatting)
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
                        //downloadModel(action.modelCode , action.name)
                        checkConnectivityAndDownloadModel(action.modelCode , action.name)
                    }

                    TranslateAction.ManagerAction.DownloadModelWithCellularData -> {
                        downloadModelOverCellular()
                    }

                    is TranslateAction.ManagerAction.FilterByDownloads -> {
                        setDownloadsFilter(action.filter)
                    }

                    //UI
                    TranslateAction.ManagerAction.DismissCellularDownloadDialog -> {
                        dismissCellularDownloadDialog()
                        resetInternalState()
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
            println("Collecting...")

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
                    updateUiModels()
                }
        }
    }

    private fun setDownloadsFilter(filter: Boolean) {
        viewModelScope.launch {
            log {
                "Filter by downloads - $filter"
            }
            _state.update {
                it.copy(
                    downloadsFilter = filter
                )
            }
            updateUiModels()
        }
    }
    private fun updateUiModels(){
        log {
            "Updating UI models"
        }
        viewModelScope.launch {
            val downloadsFilter = _state.value.downloadsFilter
            val newList = if(downloadsFilter){
                _models.value.filter {
                    it.downloaded
                }
            }else{
                _models.value
            }
            _state.update {
                it.copy(
                    filteredModels = newList
                )
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

    /**
     * Resolves translate call
     */
    private fun resolveTranslate(){
        log {
            "Resolve translate"
        }
        if(_state.value.keepFormatting){
            translateWithFormatting()
        }else{
            translate()
        }
    }

    /**
     * Translate without keeping the original formatting. Line breaks will be removed
     */
    private fun translate() {
        log {
            "Translate without formatting"
        }
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

    /**
     * Translate with the original text formatting.
     */
    private fun translateWithFormatting() {
        log {
            "Translate with formatting"
        }
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
                val textLines = _state.value.sourceText.split("\n")
                val translatedLines = textLines.map {
                    translationManager.translateText(it)
                }

                //val result = translationManager.translateText(_state.value.sourceText)
                val result = translatedLines.joinToString("\n")
                _state.update {
                    it.copy(
                        translating = false ,
                        translatedText = result
                    )
                }
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
        return if (value.srcLanguageCode == null || value.destLanguageCode == null) {
            false
        } else {
            true
        }
    }

    /**
     * Change DEST lang
     */
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
    /**
     * Change SRC lang
     */
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

    private fun setTextFormatting(keepFormatting : Boolean){
        viewModelScope.launch {
            _state.update {
                it.copy(keepFormatting = keepFormatting)
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


    /**
     * clears input methods, src, translated text
     */
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
    private fun checkConnectivityAndDownloadModel(modelCode: String , name: String) {
        viewModelScope.launch {

            if (_state.value.downloading) {
                _events.send(UIEvents.Error("Another model is being downloaded, please wait..."))
                return@launch
            }
            when (networkSpecs.type) {
                //NO NW
                NetworkType.UNKNOWN -> {
                    _events.send(UIEvents.Error("Seems your network is unstable, please try later..."))
                    return@launch
                }

                //MOBILE
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

                //ok over wifi and ethernet
                else -> {
                    downloadModel(modelCode , name)
                }
            }
        }
    }

    private fun downloadModelOverCellular() {
        viewModelScope.launch {
            dismissCellularDownloadDialog()
            if (_state.value.downloading) {
                _events.send(UIEvents.Error("Another model is being downloaded, please wait..."))
                return@launch
            }


            val modelCode = internalState.modelCode ?: return@launch
            val name = internalState.modelName ?: return@launch
            downloadModel(modelCode , name , false)
            resetInternalState()
        }
    }

    @Deprecated("Schedules download without network. Use checkConnectivityAndDownloadModel() instead")
    private suspend fun downloadModel(
        modelCode: String ,
        name: String ,
        wifiRequired: Boolean = true
    ) {

        _state.update {
            it.copy(downloading = true)
        }
        wanderaNotification.sendDownloadNotification(name)
        _events.send(UIEvents.SuccessWithMsg("$name model is being downloaded, feel free to switch to other apps in the meantime!"))
        try {

            //---- DOWNLOAD ----
            translationManager.downloadModel(languageCode = modelCode , wifiRequired = wifiRequired)

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


    private fun dismissCellularDownloadDialog() {
        viewModelScope.launch {
            _state.update {
                it.copy(cellularDataDownloadDialog = false)
            }
        }
    }

    private fun resetInternalState() {
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


    private fun observeNetwork() {
        viewModelScope.launch {
            networkObserver.isConnected
                .collect { specs ->
                    networkSpecs = specs
                }
        }

    }

    private fun log(msg : () ->String){
        if(loggingEnabled){
            Log.d("translateVm" , msg())
        }
    }

    override fun onCleared() {
        super.onCleared()
        translationManager.clearTranslator()
    }


}