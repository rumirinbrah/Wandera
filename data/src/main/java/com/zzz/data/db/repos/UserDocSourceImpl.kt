package com.zzz.data.db.repos

import com.zzz.data.db.dao.UserDocDao
import com.zzz.data.trip.model.UserDocument
import com.zzz.data.trip.source.UserDocSource
import kotlinx.coroutines.flow.Flow

internal class UserDocSourceImpl(
    private val userDocDao: UserDocDao
) :UserDocSource{

    override suspend fun addDocument(doc: UserDocument): Long {
        return userDocDao.addDocument(doc)
    }

    override suspend fun deleteDocumentById(docId: Long) {
        userDocDao.deleteDocumentById(docId)
    }

    override suspend fun updateDocument(doc: UserDocument) {
        userDocDao.updateDocument(doc)
    }

    override suspend fun updateDocumentById(docId: Long , newDocName: String) {
        userDocDao.updateDocumentById(docId,newDocName)
    }

    override fun getUserDocumentsByTripId(tripId: Long): Flow<List<UserDocument>> {
        return userDocDao.getDocumentsByTripId(tripId)
    }
}