package com.muthia0027.mobpro1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.muthia0027.mobpro1.model.Financial
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialDao {

    @Insert
    suspend fun insert(financial: Financial)

    @Update
    suspend fun update(financial: Financial)

    @Query("SELECT * FROM financial ORDER BY date DESC")
    fun getFinancial(): Flow<List<Financial>>

    @Query("SELECT * FROM financial WHERE id = :id")
    suspend fun getFinancialById(id: Long): Financial?

    @Query("DELETE FROM financial WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM financial WHERE date = :date ORDER BY date DESC")
    fun getByDate(date: String): Flow<List<Financial>>
}