package com.zzz.feature_translate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.translate.model.TranslationModel
import com.zzz.data.translate.source.TranslateSource
import com.zzz.feature_translate.manager.TranslationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslationViewModel(
    private val dataSource : TranslateSource,
    private val translationManager: TranslationManager
) :ViewModel(){

    private val _models = MutableStateFlow<List<TranslationModel>>(emptyList())
    val models = _models.asStateFlow()

    private val _state = MutableStateFlow(TranslateState())
    val state = _state.asStateFlow()

    //we use this to make sure we dont initialize translator multiple times for same ip op lang
    private var sessionOngoing : Boolean = false


    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    init {
        Log.d("translateVm" , "TranslationViewModel init...")
        collectModels()
    }

    fun onAction(action: TranslateAction){
        when(action){
            is TranslateAction.TranslatorAction->{
                when(action){
                    TranslateAction.TranslatorAction.ClearResources -> TODO()
                    is TranslateAction.TranslatorAction.CreateTranslator -> TODO()
                    is TranslateAction.TranslatorAction.TranslateText -> {
                        translate()
                    }

                    is TranslateAction.TranslatorAction.ChangeDestLanguage -> {
                        changeDestLang(action.name,action.modelCode)
                    }
                    is TranslateAction.TranslatorAction.ChangeSrcLanguage -> {
                        changeSrcLang(action.name,action.modelCode)
                    }
                }
            }
            is TranslateAction.ManagerAction->{
                when(action){
                    is TranslateAction.ManagerAction.DeleteModel -> {
                        deleteModel(action.modelCode)
                    }
                    is TranslateAction.ManagerAction.DownloadModel -> {
                        downloadModel(action.modelCode)
                    }
                }
            }
            is TranslateAction.OnTextChange ->{
                onTextChange(action.value)
            }

            TranslateAction.OnClearText -> {
                onClearText()
            }
        }
    }

    private fun collectModels(){
        viewModelScope.launch {
            dataSource.getModels()
                .flowOn(Dispatchers.IO)
                .collect{newModels->
                    _models.update {
                        newModels
                    }
                }
        }
    }

    //---------- TRANSLATOR -----------

    private fun translate(){
        viewModelScope.launch {
            if(!validateTranslatorInput()){
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
                        translating =false,
                        translatedText = result
                    )
                }
                println(result)
            }catch (e : Exception){
                _state.update {
                    it.copy(translating = false)
                }
                _events.send(UIEvents.Error(e.message ?:"An unknown error occurred while translating"))
            }

        }
    }
    //translator init
    private fun createTranslator(){
        if(!sessionOngoing){
            translationManager.createTranslator(
                _state.value.srcLanguageCode!! ,
                _state.value.destLanguageCode!!
            )
            sessionOngoing = true
        }
    }

    private fun validateTranslatorInput():Boolean{
        val value = _state.value
        if(value.srcLanguageCode == null || value.destLanguageCode == null){
            return false
        }
        return true
    }

    //change dest
    private fun changeDestLang(name : String, code : String){
        viewModelScope.launch {
            sessionOngoing = false

            _state.update {
                it.copy(
                    destLanguage = name,
                    destLanguageCode = code
                )
            }
        }
    }
    //change ip
    private fun changeSrcLang(name : String, code : String){
        viewModelScope.launch {
            sessionOngoing = false

            _state.update {
                it.copy(
                    srcLanguage = name,
                    srcLanguageCode = code
                )
            }
        }
    }
    // text field
    private fun onTextChange(value: String){
        viewModelScope.launch {
            _state.update {
                it.copy(sourceText = value)
            }
        }
    }

    private fun onClearText(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    sourceText = "",
                    translatedText = ""
                )
            }
        }
    }

    //---------- MANAGER -----------
    private fun downloadModel(modelCode : String){
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(downloading = true)
                }
                translationManager.downloadModel(modelCode)

                //set model true in db
                dataSource.updateModelStatus(modelCode,true)


                _state.update {
                    it.copy(downloading = false)
                }
                _events.send(UIEvents.SuccessWithMsg("Model has been downloaded successfully!"))

            }catch (e : Exception){
                //errur
                _state.update {
                    it.copy(downloading = false)
                }
                _events.send(UIEvents.Error(e.message ?:"An unknown error occurred downloading model"))
            }
        }
    }
    private fun deleteModel(modelCode: String){
        if(_state.value.deleting){
            return
        }
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(deleting = true)
                }
                translationManager.deleteModel(modelCode)

                //set model false in db
                dataSource.updateModelStatus(modelCode,false)


                _state.update {
                    it.copy(deleting = false)
                }
                _events.send(UIEvents.SuccessWithMsg("Model has been deleted successfully!"))
            }catch (e : Exception){
                //errur
                _state.update {
                    it.copy(deleting = false)
                }
                _events.send(UIEvents.Error(e.message ?:"An unknown error occurred deleting model"))
            }
        }
    }



}