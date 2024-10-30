package com.example.movieapp.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.model.Movie
import com.example.movieapp.core.ui.asMediaTypeModel
import com.example.movieapp.core.ui.asMovie
import com.example.movieapp.core.ui.asMovieMediaType
import com.example.movieapp.core.ui.pagingMap
import com.example.movieapp.navigation.ListDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

data class ListUiState(
    val mediaType: MediaType.Common,
    val movies: Flow<PagingData<Movie>>,
)

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getMoviePagingUseCase: GetMoviePagingUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(getInitialUiState(savedStateHandle))
    val uiState = _uiState.asStateFlow()

    private fun getInitialUiState(savedStateHandle: SavedStateHandle): ListUiState {
        val mediaType = ListDestination.fromSavedStateHandle(savedStateHandle)

        val movies = getMoviePagingUseCase(mediaType.asMovieMediaType().asMediaTypeModel())
            .pagingMap(MovieModel::asMovie)
            .cachedIn(viewModelScope)

        return ListUiState(mediaType = mediaType, movies = movies)
    }
}