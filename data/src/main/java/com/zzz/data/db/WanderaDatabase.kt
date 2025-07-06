package com.zzz.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zzz.data.db.dao.DayDao
import com.zzz.data.db.dao.TodoDao
import com.zzz.data.db.dao.TripDao
import com.zzz.data.db.dao.UserDocDao
import com.zzz.data.db.dao.notes.NotesDao
import com.zzz.data.db.dao.translate.TranslateDao
import com.zzz.data.db.type_conv.UriTypeConverter
import com.zzz.data.note.model.ExpenseNote
import com.zzz.data.translate.model.TranslationModel
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.model.UserDocument

@Database(
    entities = [
        Trip::class ,
        Day::class ,
        TodoLocation::class ,
        UserDocument::class,
        TranslationModel ::class,
        ExpenseNote :: class
    ] ,
    version = 1 ,
    exportSchema = false
)
@TypeConverters(value = [UriTypeConverter::class])
internal abstract class WanderaDatabase : RoomDatabase() {

    abstract val tripDao: TripDao
    abstract val dayDao: DayDao
    abstract val todoDao: TodoDao
    abstract val userDocDao : UserDocDao

    abstract val translateDao : TranslateDao

    abstract val notesDao : NotesDao

}