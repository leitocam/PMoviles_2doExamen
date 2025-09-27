package com.calyrsoft.ucbp1.features.dollar.data.repository

import com.calyrsoft.ucbp1.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.calyrsoft.ucbp1.features.dollar.data.datasource.DollarLocalDataSource
import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel
import com.calyrsoft.ucbp1.features.dollar.data.mapper.toDomain
import com.calyrsoft.ucbp1.features.dollar.data.database.dao.DollarDao
import com.calyrsoft.ucbp1.features.dollar.data.mapper.toEntity

import com.calyrsoft.ucbp1.features.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.map

class DollarRepositoryImpl(
    private val dao: DollarDao
) : DollarRepository {

    override fun observeLatest(): Flow<DollarModel?> =
        dao.observeLatest().map { it?.toDomain() }

    override fun observeHistory(): Flow<List<DollarModel>> =
        dao.observeHistory().map { list -> list.map { it.toDomain() } }

    override suspend fun saveSnapshot(model: DollarModel) {
        dao.insertSnapshot(model.toEntity())
    }
}