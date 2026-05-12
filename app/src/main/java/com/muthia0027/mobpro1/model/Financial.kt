package com.muthia0027.mobpro1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial")
data class Financial(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val amount: Int,
    val category: String,
    val date: String
)