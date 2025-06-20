package com.zzz.feature_translate.presentation.viewmodel

sealed class TranslateAction {

    sealed class ManagerAction : TranslateAction(){
        data class DownloadModel(val modelCode :String) : ManagerAction()
        data class DeleteModel(val modelCode :String) : ManagerAction()
    }

    sealed class TranslatorAction : TranslateAction(){
        data class ChangeSrcLanguage(val name : String,val modelCode: String) : TranslatorAction()
        data class ChangeDestLanguage(val name: String,val modelCode: String) : TranslatorAction()
        data object TranslateText : TranslatorAction()

        data object ClearResources : TranslatorAction()
        data class CreateTranslator(val source : String , val target : String) : TranslatorAction()
    }

    data class OnTextChange(val value :String) : TranslateAction()
    data object OnClearText : TranslateAction()

}