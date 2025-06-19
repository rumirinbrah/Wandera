package com.zzz.data.db.repos.translate

import com.zzz.data.db.dao.translate.TranslateDao
import com.zzz.data.translate.model.TranslationModel
import com.zzz.data.translate.source.TranslateSource
import kotlinx.coroutines.flow.Flow

internal class TranslateSourceImpl(
    private val dao: TranslateDao
) : TranslateSource {

    override suspend fun addModel(model: TranslationModel): Long {
        return dao.addModel(model)
    }

    override suspend fun updateModelStatus(modelCode: String , downloaded: Boolean) {
        dao.updateModelStatus(modelCode,downloaded)
    }

    override suspend fun deleteModel(modelCode: String) {
        dao.deleteModel(modelCode)
    }

    override fun getModels(): Flow<List<TranslationModel>> {
        return dao.getModels()
    }
}