package com.calyrsoft.ucbp1.features.movies.domain.repository

import com.calyrsoft.ucbp1.features.movies.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow
interface IMoviesRepository {
    suspend fun fetchMovies() : Result<Array<MovieModel>>
    fun observePopularSorted(): Flow<List<MovieModel>>
    suspend fun setLike(movieId: Long, liked: Boolean)
}