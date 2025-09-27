package com.calyrsoft.ucbp1.features.dollar.data.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.dollar.data.database.entity.DollarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DollarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(entity: DollarEntity)

    @Query("SELECT * FROM dollar_history ORDER BY updatedAt DESC")
    fun observeHistory(): Flow<List<DollarEntity>>

    @Query("SELECT * FROM dollar_history ORDER BY updatedAt DESC LIMIT 1")
    fun observeLatest(): Flow<DollarEntity?>
}   