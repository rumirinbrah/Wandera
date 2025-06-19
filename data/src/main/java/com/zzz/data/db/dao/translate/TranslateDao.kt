package com.zzz.data.db.dao.translate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zzz.data.translate.model.TranslationModel
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TranslateDao {

    //add
    @Insert()
    abstract suspend fun addModel(model: TranslationModel): Long

    //update
    @Update
    abstract suspend fun updateModel(model: TranslationModel)

    @Query("update translation_table set downloaded = :downloaded where languageCode = :modelCode")
    abstract suspend fun updateModelStatus(modelCode: String , downloaded : Boolean)

    //delete
    @Query("delete from translation_table where languageCode = :modelCode")
    abstract suspend fun deleteModel(modelCode: String)

    //get
    @Query("select * from translation_table order by name ASC")
    abstract fun getModels(): Flow<List<TranslationModel>>


}