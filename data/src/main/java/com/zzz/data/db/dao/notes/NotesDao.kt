package com.zzz.data.db.dao.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zzz.data.note.model.ChecklistEntity
import com.zzz.data.note.model.ExpenseNote
import kotlinx.coroutines.flow.Flow

/**
 * Contains DB methods for both ExpenseNote and ChecklistEntity entity
 */
@Dao
abstract class NotesDao {

    //--------- EXPENSE NOTE ----------
    @Insert(entity = ExpenseNote::class)
    abstract suspend fun addExpenseNote(note : ExpenseNote) : Long

    @Query("UPDATE expense_table set text = :text where id = :noteId")
    abstract suspend fun updateExpenseNote(noteId : Long , text : String)

    @Query("SELECT * from expense_table where tripId = :tripId")
    abstract suspend fun getExpenseNote(tripId : Long) : ExpenseNote


    //--------- Checklist ----------
    @Insert(entity = ChecklistEntity::class)
    abstract suspend fun addChecklistEntity(item: ChecklistEntity) : Long

    @Query("update checklist_table set title = :title where id = :itemId")
    abstract suspend fun updateChecklistEntity(itemId : Long , title : String)

    @Query("update checklist_table set isChecked = :checked where id = :itemId")
    abstract suspend fun checkChecklistEntity(itemId: Long , checked : Boolean)

    @Query("DELETE from checklist_table where id = :itemId")
    abstract suspend fun deleteChecklistEntity(itemId: Long)

    @Query("SELECT * FROM checklist_table where tripId = :tripId")
    abstract fun getChecklistByTripId(tripId: Long) : Flow<List<ChecklistEntity>>

}