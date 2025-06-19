package com.zzz.data.translate.source

import com.zzz.data.translate.model.TranslationModel
import kotlinx.coroutines.flow.Flow

interface TranslateSource {

    suspend fun addModel(model: TranslationModel) : Long

    suspend fun updateModel(model: TranslationModel)

    suspend fun deleteModel(modelCode : String)

    fun getModels() : Flow<List<TranslationModel>>

}