package com.zzz.feature_translate.presentation.viewmodel

import com.zzz.data.translate.model.TranslationModel


data class TranslateState(
    val downloadsFilter : Boolean = false ,
    val filteredModels : List<TranslationModel> = emptyList() ,
    val downloading : Boolean = false ,
    val deleting : Boolean = false ,
    val modelToDelete : String? = null ,
    val modelToDeleteCode : String? = null ,
    val isFirstTime : Boolean = false ,
    val cellularDataDownloadDialog : Boolean = false ,
    val translating : Boolean = false ,
    val loading : Boolean = false ,
    val sourceText : String = "" ,
    val translatedText : String = "" ,
    val keepFormatting : Boolean = false,
    val srcLanguage : String = "" ,
    val srcLanguageCode : String? = null ,
    val destLanguage : String = "" ,
    val destLanguageCode : String? = null ,

    )
