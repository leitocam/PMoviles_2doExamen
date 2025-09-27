package com.calyrsoft.ucbp1.features.movies.data.repository

import android.util.Log
import com.calyrsoft.ucbp1.features.movies.data.database.IMovieLikeDao
import com.calyrsoft.ucbp1.features.movies.data.database.entity.MovieLikeEntity
import com.calyrsoft.ucbp1.features.movies.data.datasource.MovieLocalDataSource
import com.calyrsoft.ucbp1.features.movies.data.datasource.ThemoviedbDataSource
import com.calyrsoft.ucbp1.features.movies.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movies.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class MoviesRepository(
    val remoteDataSource: ThemoviedbDataSource,
    val localDataSource: MovieLocalDataSource,
    private val likeDao: IMovieLikeDao // <- NUEVO
) : IMoviesRepository {


    override suspend fun fetchMovies(): Result<Array<MovieModel>> {
        val response = remoteDataSource.getMovies()
        response.fold(
            onSuccess = { movieDtoArray ->
                val movieModelArray = movieDtoArray.map {
                    MovieModel(it.title, it.pathUrl) // id y liked se rellenan por default
                }.toTypedArray()

                try {
                    localDataSource.insertMovies(movieModelArray.toList())
                } catch (e: Exception) {
                    Log.e("MoviesRepository", "Error saving movies to local DB", e)
                }
                return Result.success(movieModelArray)
            },
            onFailure = { exception ->
                return Result.failure(exception)
            }
        )
    }

    // ----------------- NUEVO: flujo ordenado (liked primero) -----------------
    override fun observePopularSorted(): Flow<List<MovieModel>> =
        combine(
            flow {
                // Reusa tu datasource remoto actual (o local si prefieres cache)
                val res = remoteDataSource.getMovies()
                val list = res.getOrElse { emptyArray() }.map {
                    MovieModel(it.title, it.pathUrl)
                }
                emit(list)
            },
            likeDao.observeLikedIds()
        ) { apiList, likedIds ->
            apiList
                .map { m -> m.copy(liked = likedIds.contains(m.id)) }
                .sortedWith(
                    compareByDescending<MovieModel> { it.liked } // liked primero
                        .thenBy { it.title }
                )
        }

    override suspend fun setLike(movieId: Long, liked: Boolean) {
        likeDao.upsert(MovieLikeEntity(movieId = movieId, liked = liked))
    }
}
