package com.zzz.feature_translate.presentation.viewmodel

sealed class TranslateAction {

    sealed class ManagerAction : TranslateAction(){
        data class FilterByDownloads(val filter : Boolean) : ManagerAction()
        data object DismissCellularDownloadDialog : ManagerAction()

        data class DownloadModel(val modelCode :String, val name: String) : ManagerAction()
        data object DownloadModelWithCellularData : ManagerAction()
        data class SetModelToDelete(val modelCode :String?,val name :String?) : ManagerAction()

        data object DeleteModel : ManagerAction()
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