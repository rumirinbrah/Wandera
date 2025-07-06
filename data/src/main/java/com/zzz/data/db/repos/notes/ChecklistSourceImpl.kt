package com.zzz.data.db.repos.notes

import com.zzz.data.db.dao.notes.NotesDao
import com.zzz.data.note.model.ChecklistEntity
import com.zzz.data.note.source.ChecklistSource
import kotlinx.coroutines.flow.Flow

internal class ChecklistSourceImpl(
    private val notesDao: NotesDao
) : ChecklistSource {

    override suspend fun addItem(entity: ChecklistEntity): Long {
        return notesDao.addChecklistEntity(entity)
    }

    override suspend fun updateItem(itemId: Long , title: String) {
        notesDao.updateChecklistEntity(itemId, title)
    }

    override suspend fun checkItem(itemId: Long , checked: Boolean) {
        notesDao.checkChecklistEntity(itemId, checked)
    }

    override suspend fun deleteItem(itemId: Long) {
        notesDao.deleteChecklistEntity(itemId)
    }

    override fun getChecklistItems(tripId : Long): Flow<List<ChecklistEntity>> {
        return notesDao.getChecklistByTripId(tripId)
    }
}