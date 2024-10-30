package com.example.movieapp.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.model.MovieDetails
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.ui.EventHandler
import com.example.movieapp.core.ui.asMovieDetails
import com.example.movieapp.navigation.DetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.movieapp.R
import com.example.movieapp.core.ui.asMovieDetailModel

data class DetailsUiState(
    val mediaType: MediaType.Details,
    val movie: MovieDetails? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val errorMessage: String? = null,
    val isOfflineModeAvailable: Boolean = false,
    val isWishListed : Boolean = false,
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val addMovieToWishlistUseCase: AddMovieToWishlistUseCase,
    private val removeMovieFromWishlistUseCase: RemoveMovieFromWishlistUseCase,
    private val getDetailMovieUseCase: GetDetailMovieUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), EventHandler<DetailsEvent>{

    private val _uiState = MutableStateFlow(getInitialUiState(savedStateHandle))
    val uiState = _uiState.asStateFlow()

    private fun getInitialUiState(savedStateHandle: SavedStateHandle): DetailsUiState {
        val mediaType = DetailsDestination.fromSavedStateHandle(savedStateHandle)
        return DetailsUiState(mediaType = mediaType)
    }

    init {
        loadContent()
    }

    override fun onEvent(event: DetailsEvent) = when (event) {
        DetailsEvent.WishlistMovie -> onWishlistMovie()
        DetailsEvent.Refresh -> onRefresh()
        DetailsEvent.Retry -> onRetry()
        DetailsEvent.ClearUserMessage -> onClearUserMessage()
    }

    private fun loadContent() = when (val mediaType = uiState.value.mediaType) {
        is MediaType.Details.Movie -> loadMovie(mediaType.id)
        is MediaType.Details.TvShow -> loadTv(mediaType.id)
        is MediaType.Details.Trailers -> loadMovie(mediaType.id)
    }

    private fun onRefresh() {
        viewModelScope.coroutineContext.cancelChildren()
        loadContent()
    }

    private fun onRetry() {
        onClearErrorMessage()
        onRefresh()
    }

    private fun onClearErrorMessage() = _uiState.update { it.copy(errorMessage = null) }
    private fun onClearUserMessage() = _uiState.update { it.copy(userMessage = null) }

    fun onWishlistMovie() {
        _uiState.update {
            it.copy(movie = it.movie?.copy(isWishListed = !it.movie.isWishListed))
        }
        viewModelScope.launch {
            uiState.value.movie?.let { movie ->
                if (movie.isWishListed) {
                    addMovieToWishlistUseCase(movie.asMovieDetailModel())
                    setUserMessage(R.string.add_movie_wishlist)
                } else {
                    removeMovieFromWishlistUseCase(movie.id)
                    setUserMessage(R.string.remove_movie_wishlist)
                }
            }
        }
    }

    fun snackBarMessageShown(){
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    private fun setUserMessage(userMessage: Int){
        _uiState.update {
            it.copy(userMessage = userMessage)
        }
    }

    private fun loadTv(id: Int) = viewModelScope.launch {

    }

    private fun loadMovie(id: Int) = viewModelScope.launch {
        getDetailMovieUseCase(id).collect{ response ->
            when(response) {
                is CinemaxResponse.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is CinemaxResponse.Success -> {
                    //Log.d("DetailResponse",response.value?.title.toString())
                    _uiState.update { it.copy(movie = response.value?.asMovieDetails(), isLoading = false) }
                }
                is CinemaxResponse.Failure -> {
                    handleFailure(response.error)
                }
            }
        }
    }

    private fun handleFailure(error: String) = _uiState.update {
        it.copy(
            errorMessage = error,
            isOfflineModeAvailable = it.movie != null,
            isLoading = false
        )
    }

}