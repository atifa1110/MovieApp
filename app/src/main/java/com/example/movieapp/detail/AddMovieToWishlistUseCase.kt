package com.example.movieapp.detail

import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.network.repository.WishListRepository
import javax.inject.Inject

class AddMovieToWishlistUseCase @Inject constructor(
    private val repository: WishListRepository
) {
    suspend operator fun invoke(movie : MovieDetailModel) = repository.addMovieToWishlist(movie)
}