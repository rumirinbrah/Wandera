package com.zzz.feature_translate.presentation.viewmodel

import com.zzz.data.translate.model.TranslationModel


data class TranslateState(
    val downloading : Boolean = false ,
    val deleting : Boolean = false ,
    val downloadsFilter : Boolean = false,
    val downloadedModels : List<TranslationModel>? = null,
    val translating : Boolean = false ,
    val loading : Boolean = false ,
    val sourceText : String = "" ,
    val translatedText : String = "" ,
    val srcLanguage : String = "" ,
    val srcLanguageCode : String? = null ,
    val destLanguage : String = "" ,
    val destLanguageCode : String? = null ,
    val modelToDelete : String? = null,
    val modelToDeleteCode : String? = null,
    val isFirstTime : Boolean = false
)
