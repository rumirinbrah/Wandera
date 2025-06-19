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
                    is TranslateAction.TranslatorAction.TranslateText -> TODO()
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

    private fun downloadModel(modelCode : String){
        viewModelScope.launch {
            try {
                translationManager.downloadModel(modelCode)
                _events.send(UIEvents.Success)
            }catch (e : Exception){
                //errur
                _events.send(UIEvents.Error(e.message ?:"An unknown error occurred downloading model"))
            }
        }
    }
    private fun deleteModel(modelCode: String){
        viewModelScope.launch {
            try {
                translationManager.deleteModel(modelCode)
                _events.send(UIEvents.Success)
            }catch (e : Exception){
                //errur
                _events.send(UIEvents.Error(e.message ?:"An unknown error occurred deleting model"))
            }
        }
    }



}