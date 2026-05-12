package com.muthia0027.mobpro1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muthia0027.mobpro1.model.Financial

@Database(entities = [Financial::class], version = 1, exportSchema = false)
abstract class FinancialDb: RoomDatabase() {

    abstract val dao: FinancialDao

    companion object {
        @Volatile
        private var INSTANCE: FinancialDb? = null

        fun getInstance(context: Context): FinancialDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FinancialDb::class.java,
                        "financial.db"
                    )
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}