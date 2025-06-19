package com.zzz.data.translate.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zzz.data.util.DbUtils

//60 models (06/25)
@Entity(
    tableName = DbUtils.TRANSLATION_TABLE_NAME
)
data class TranslationModel(
    @PrimaryKey
    val languageCode : String,
    val name : String,
    val downloaded : Boolean = false
)
