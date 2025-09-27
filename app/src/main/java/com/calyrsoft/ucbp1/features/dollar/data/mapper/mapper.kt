package com.calyrsoft.ucbp1.features.dollar.data.mapper

import com.calyrsoft.ucbp1.features.dollar.data.database.entity.DollarEntity
import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel

fun DollarEntity.toDomain() = DollarModel(
    oficialCompra = oficialCompra,
    oficialVenta = oficialVenta,
    paraleloCompra = paraleloCompra,
    paraleloVenta = paraleloVenta,
    updatedAt = updatedAt
)

fun DollarModel.toEntity() = DollarEntity(
    oficialCompra = oficialCompra,
    oficialVenta = oficialVenta,
    paraleloCompra = paraleloCompra,
    paraleloVenta = paraleloVenta,
    updatedAt = updatedAt
)