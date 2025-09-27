package com.calyrsoft.ucbp1.features.dollar.data.repository

import com.calyrsoft.ucbp1.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel
import com.calyrsoft.ucbp1.features.dollar.data.database.dao.DollarDao
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow

interface DollarRepository {
    fun observeLatest(): Flow<DollarModel?>
    fun observeHistory(): Flow<List<DollarModel>>
    suspend fun saveSnapshot(model: DollarModel)
}