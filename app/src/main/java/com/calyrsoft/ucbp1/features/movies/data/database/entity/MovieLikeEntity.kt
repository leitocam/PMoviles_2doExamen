package com.calyrsoft.ucbp1.features.movies.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_likes")
data class MovieLikeEntity(
    @PrimaryKey val movieId: Long,
    val liked: Boolean
)
