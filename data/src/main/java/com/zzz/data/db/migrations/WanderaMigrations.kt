package com.zzz.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zzz.data.util.DbUtils

object WanderaMigrations {

    /**
     * Added column currencySymbol to track_expense_table
     */
    val MIGRATION_1_2 = object : Migration(1,2){
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                    ALTER TABLE ${DbUtils.TRACK_EXPENSE_TABLE_NAME} 
                    ADD COLUMN currencySymbol TEXT NOT NULL DEFAULT '$'
                """.trimIndent()
            )
        }
    }

}