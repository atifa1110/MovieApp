package com.example.movieapp.wishlist.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.model.WishList
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.core.ui.asWishlist
import com.example.movieapp.wishlist.usecase.DeleteMovieFromWishlistUseCase
import com.example.movieapp.wishlist.usecase.DeleteTvShowFromWishlistUseCase
import com.example.movieapp.wishlist.usecase.GetWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WishlistUiState(
    val wishlist: List<WishList> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: MutableState<Int?> = mutableStateOf(null),
)

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val deleteMovieFromWishlistUseCase: DeleteMovieFromWishlistUseCase,
    private val deleteTvShowFromWishlistUseCase: DeleteTvShowFromWishlistUseCase
) :ViewModel(){

    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadWishlist()
    }

    private fun loadWishlist() = viewModelScope.launch {
        getWishlistUseCase.invoke().collect{ response ->
            when(response){
                is CinemaxResponse.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is CinemaxResponse.Success -> {
                    val result = response.value.map(WishlistModel::asWishlist)
                    _uiState.update { it.copy(isLoading = false, wishlist = result) }
                }
                is CinemaxResponse.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun deleteMovieFromWishlist(id: Int) = viewModelScope.launch {
        deleteMovieFromWishlistUseCase.invoke(id)
        loadWishlist()
        showSnackBarMessage(R.string.success_delete_from_wishlist)
    }

    fun deleteTvShowFromWishlist(id: Int) = viewModelScope.launch {
        deleteTvShowFromWishlistUseCase.invoke(id)
        loadWishlist()
        showSnackBarMessage(R.string.success_delete_from_wishlist)
    }


    fun snackBarMessageShown() {
        _uiState.update { currentState ->
            currentState.userMessage.value = null
            currentState
        }
    }


    private fun showSnackBarMessage(message: Int) {
        _uiState.update { currentState ->
            currentState.userMessage.value = message
            currentState
        }
    }
}