package com.calyrsoft.ucbp1.features.movies.domain.usecase

import com.calyrsoft.ucbp1.features.movies.domain.repository.IMoviesRepository

class SetMovieLikeUseCase(
    private val repo: IMoviesRepository
) {
    suspend operator fun invoke(movieId: Long, liked: Boolean) =
        repo.setLike(movieId, liked)
}
