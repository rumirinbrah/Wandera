package com.zzz.feature_translate.presentation.viewmodel

sealed class TranslateAction {

    sealed class ManagerAction : TranslateAction(){
        data class DownloadModel(val modelCode :String) : ManagerAction()
        data class DeleteModel(val modelCode :String) : ManagerAction()
    }

    sealed class TranslatorAction : TranslateAction(){
        data object ClearResources : TranslatorAction()
        data class CreateTranslator(val source : String , val target : String) : TranslatorAction()
        data class TranslateText(val text : String ) : TranslatorAction()
    }

}