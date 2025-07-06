package com.zzz.data.db.dao.notes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zzz.data.note.model.ExpenseNote

@Dao
abstract class NotesDao {

    //--------- EXPENSE NOTE ----------
    @Insert(entity = ExpenseNote::class)
    abstract suspend fun addExpenseNote(note : ExpenseNote) : Long

    @Query("UPDATE expense_table set text = :text where id = :noteId")
    abstract suspend fun updateExpenseNote(noteId : Long , text : String)

    @Query("SELECT * from expense_table where tripId = :tripId")
    abstract suspend fun getExpenseNote(tripId : Long) : ExpenseNote

}