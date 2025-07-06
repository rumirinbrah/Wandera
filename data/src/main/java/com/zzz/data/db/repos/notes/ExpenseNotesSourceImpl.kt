package com.zzz.data.db.repos.notes

import com.zzz.data.db.dao.notes.NotesDao
import com.zzz.data.note.model.ExpenseNote
import com.zzz.data.note.source.ExpenseNoteSource

internal class ExpenseNotesSourceImpl(
    private val notesDao: NotesDao
) : ExpenseNoteSource {

    override suspend fun addNote(note: ExpenseNote): Long {
        return notesDao.addExpenseNote(note)
    }

    override suspend fun updateNote(noteId: Long , text: String) {
        notesDao.updateExpenseNote(noteId, text)
    }

    override suspend fun getNote(tripId: Long): ExpenseNote {
        return notesDao.getExpenseNote(tripId)
    }
}