package com.example.movieapp.wishlist

import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.network.repository.WishListRepository
import javax.inject.Inject

class DeleteMovieFromWishlistUseCase @Inject constructor(
    private val repository: WishListRepository
) {
    suspend operator fun invoke(id: Int) =
        repository.removeMovieFromWishlist(id)
}