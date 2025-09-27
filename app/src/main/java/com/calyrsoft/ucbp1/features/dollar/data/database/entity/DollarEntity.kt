package com.calyrsoft.ucbp1.features.dollar.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dollar_history")
data class DollarEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val oficialCompra: Double,
    val oficialVenta: Double,
    val paraleloCompra: Double,
    val paraleloVenta: Double,
    val updatedAt: Long
)