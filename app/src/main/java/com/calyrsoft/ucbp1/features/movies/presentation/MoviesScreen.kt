package com.calyrsoft.ucbp1.features.movies.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.calyrsoft.ucbp1.features.movies.domain.model.MovieModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    vm: MoviesViewModel = koinViewModel(),
    navController: NavController,
    modifier: Modifier
) {
    val state by vm.state.collectAsState()

    // Cargamos populares al entrar
    LaunchedEffect(Unit) { vm.fetchMovies() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val currentState = state) {
            is MoviesViewModel.MoviesStateUI.Init -> {
                Text("Initializing...")
            }
            is MoviesViewModel.MoviesStateUI.Loading -> {
                CircularProgressIndicator()
            }
            is MoviesViewModel.MoviesStateUI.Error -> {
                Text(
                    text = "Error: ${currentState.message}",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
            is MoviesViewModel.MoviesStateUI.Success -> {
                val sorted = currentState.movies
                    .toList()
                    .sortedWith(
                        compareByDescending<MovieModel> { it.liked }.thenBy { it.title }
                    )
                if (sorted.isEmpty()) {
                    Text(text = "No movies found.", textAlign = TextAlign.Center)
                } else {
                    MovieList(
                        movies = sorted,
                        onToggleLike = { id, newLiked -> vm.onToggleLike(id, newLiked) }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieList(
    movies: List<MovieModel>,
    modifier: Modifier = Modifier,
    onToggleLike: (id: Long, liked: Boolean) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = movies,
            key = { movie ->
                // usa id estable si está; si no, fallback
                (movie.id.takeIf { it != 0L }?.toString()) ?: (movie.title + "::" + (movie.pathUrl ?: ""))
            }
        ) { movie ->
            MovieItem(
                movie = movie,
                onToggleLike = onToggleLike
            )
        }
    }
}

@Composable
fun MovieItem(
    movie: MovieModel,
    modifier: Modifier = Modifier,
    onToggleLike: (id: Long, liked: Boolean) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            val imageBaseUrl = "https://image.tmdb.org/t/p/w342"
            val path = movie.pathUrl ?: ""
            val imageUrl = if (path.startsWith("http")) path else imageBaseUrl + path

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 150.dp, height = 225.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título + botón like
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    maxLines = 2
                )
                IconButton(onClick = { onToggleLike(movie.id, !movie.liked) }) {
                    if (movie.liked) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Liked"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like"
                        )
                    }
                }
            }
        }
    }
}
