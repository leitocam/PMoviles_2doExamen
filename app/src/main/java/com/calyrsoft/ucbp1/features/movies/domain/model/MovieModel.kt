package com.calyrsoft.ucbp1.features.movies.domain.model

data class MovieModel(
    val title: String,
    val pathUrl: String?,
    val id: Long = stableIdFrom(title, pathUrl), // NEW (retrocompatible)
    val liked: Boolean = false                   // NEW
)

private fun stableIdFrom(title: String, pathUrl: String?): Long =
    (title + "|" + (pathUrl ?: "")).hashCode().toLong()
