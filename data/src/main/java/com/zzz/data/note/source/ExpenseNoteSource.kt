package com.zzz.data.note.source

import com.zzz.data.note.model.ExpenseNote

interface ExpenseNoteSource {

    suspend fun addNote(note : ExpenseNote) : Long

    suspend fun updateNote(noteId : Long, text : String)

    suspend fun getNote(tripId : Long) : ExpenseNote

}