package com.zzz.data.trip.source

import com.zzz.data.trip.model.UserDocument
import kotlinx.coroutines.flow.Flow

interface UserDocSource {

    suspend fun addDocument(doc : UserDocument) : Long

    suspend fun deleteDocumentById(docId : Long)

    suspend fun updateDocument(doc: UserDocument)
    suspend fun updateDocumentById(docId: Long, newDocName : String)

    //get
    fun getUserDocumentsByTripId(tripId : Long) : Flow<List<UserDocument>>


}