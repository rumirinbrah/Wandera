package com.zzz.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zzz.data.trip.model.UserDocument
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class UserDocDao {

    //add
    @Insert()
    abstract suspend fun addDocument(document: UserDocument) : Long

    //update
    @Update()
    abstract suspend fun updateDocument(document: UserDocument)

    @Query("update document_table set docName =:newDocName where id = :docId")
    abstract suspend fun updateDocumentById(docId : Long, newDocName : String)

    //delete
    @Query("delete from document_table where id = :docId")
    abstract suspend fun deleteDocumentById(docId: Long)

    //GET
    @Query("select * from document_table where tripId = :tripId")
    abstract fun getDocumentsByTripId(tripId : Long) : Flow<List<UserDocument>>



}