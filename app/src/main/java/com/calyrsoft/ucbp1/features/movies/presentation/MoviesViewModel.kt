package com.calyrsoft.ucbp1.features.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.movies.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movies.domain.usecase.FetchMoviesUseCase
import com.calyrsoft.ucbp1.features.movies.domain.usecase.ObservePopularSortedUseCase
import com.calyrsoft.ucbp1.features.movies.domain.usecase.SetMovieLikeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val fetchMoviesUseCase: FetchMoviesUseCase,
    observePopularSortedUseCase: ObservePopularSortedUseCase, // <- NUEVO
    private val setMovieLikeUseCase: SetMovieLikeUseCase      // <- NUEVO
) : ViewModel() {

    // --- Tu estado existente para la carga inicial desde red/local ---
    sealed class MoviesStateUI {
        object Init : MoviesStateUI()
        object Loading : MoviesStateUI()
        class Error(val message: String) : MoviesStateUI()
        class Success(val movies: Array<MovieModel>) : MoviesStateUI()
    }

    private val _state = MutableStateFlow<MoviesStateUI>(MoviesStateUI.Init)
    val state: StateFlow<MoviesStateUI> = _state.asStateFlow()

    // --- NUEVO: flujo siempre actualizado y ORDENADO (liked primero) ---
    // Úsalo en la UI para pintar la lista final.
    val moviesSorted: StateFlow<List<MovieModel>> =
        observePopularSortedUseCase()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // --- Igual que antes: dispara la carga inicial ---
    fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = MoviesStateUI.Loading
            val result = fetchMoviesUseCase.invoke()
            result.fold(
                onSuccess = { movies ->
                    _state.value = MoviesStateUI.Success(movies)
                    // No hace falta nada más: moviesSorted reaccionará al like y reordenará.
                },
                onFailure = { error ->
                    _state.value = MoviesStateUI.Error(message = error.message ?: "Error desconocido")
                }
            )
        }
    }

    // --- NUEVO: acción de like / unlike ---
    fun onToggleLike(movieId: Long, newLiked: Boolean) {
        viewModelScope.launch {
            setMovieLikeUseCase(movieId, newLiked)
            // El Flow moviesSorted re-emite y reordena automáticamente.
        }
    }
}
