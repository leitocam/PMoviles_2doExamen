package com.calyrsoft.ucbp1.features.movies.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.movies.data.database.entity.MovieLikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IMovieLikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(e: MovieLikeEntity)

    @Query("SELECT movieId FROM movie_likes WHERE liked = 1")
    fun observeLikedIds(): Flow<List<Long>>
}
