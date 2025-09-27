package com.calyrsoft.ucbp1.features.dollar.domain.model

data class DollarModel(
    val oficialCompra: Double,
    val oficialVenta: Double,
    val paraleloCompra: Double,
    val paraleloVenta: Double,
    val updatedAt: Long
)