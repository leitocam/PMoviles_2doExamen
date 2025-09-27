package com.calyrsoft.ucbp1.features.movies.domain.usecase

import com.calyrsoft.ucbp1.features.movies.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movies.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow

class ObservePopularSortedUseCase(
    private val repo: IMoviesRepository
) {
    operator fun invoke(): Flow<List<MovieModel>> = repo.observePopularSorted()
}
