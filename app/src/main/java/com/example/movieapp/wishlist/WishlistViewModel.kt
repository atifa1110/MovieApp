package com.example.movieapp.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.model.MovieDetails
import com.example.movieapp.core.model.WishList
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.ui.EventHandler
import com.example.movieapp.core.ui.asMovieDetails
import com.example.movieapp.core.ui.asWishlist
import com.example.movieapp.detail.AddMovieToWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WishlistUiState(
    val movies: List<WishList> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage : Int? = null
)

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistMoviesUseCase: GetMovieWishlistUseCase,
    private val deleteFromWishlistUseCase: DeleteMovieFromWishlistUseCase
) :ViewModel(){

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _getWishList = getWishlistMoviesUseCase()

    val uiState: StateFlow<WishlistUiState> = combine(
         _isLoading, _userMessage, _getWishList
    ) { isLoading, userMessage, wishList->
        when (wishList) {
            CinemaxResponse.Loading -> {
                WishlistUiState(isLoading = true)
            }
            is CinemaxResponse.Success -> {
                val result = wishList.value.map(WishlistModel::asWishlist)
                WishlistUiState(isLoading = isLoading, movies = result, userMessage = userMessage)
            }
            is CinemaxResponse.Failure -> {
                WishlistUiState(isLoading = true, userMessage = R.string.unexpected_error)
            }
        }
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WishlistUiState(isLoading = true)
        )


    fun deleteFromWishlist(id: Int) = viewModelScope.launch {
        deleteFromWishlistUseCase.invoke(id)
        showSnackBarMessage(R.string.success_delete_from_wishlist)
    }

    fun snackBarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackBarMessage(message: Int) {
        _userMessage.value = message
    }
}