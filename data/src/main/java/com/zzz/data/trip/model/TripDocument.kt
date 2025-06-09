package com.zzz.data.trip.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zzz.data.util.DbUtils

@Entity(
    tableName = DbUtils.DOCUMENT_TABLE_NAME ,
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ] ,
    indices = [
        Index(value = ["tripId"])
    ]
)
data class UserDocument(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0 ,
    val docName: String = "untitled" ,
    val uri: Uri ,
    val tripId: Long
)

