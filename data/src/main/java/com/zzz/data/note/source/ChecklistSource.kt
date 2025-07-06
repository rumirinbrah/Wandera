package com.zzz.data.note.source

import com.zzz.data.note.model.ChecklistEntity
import kotlinx.coroutines.flow.Flow

interface ChecklistSource {

    suspend fun addItem(entity : ChecklistEntity) : Long

    suspend fun updateItem(itemId : Long, title : String)

    suspend fun checkItem(itemId: Long , checked : Boolean)

    suspend fun deleteItem(itemId: Long)

    fun getChecklistItems(tripId : Long) : Flow<List<ChecklistEntity>>

}