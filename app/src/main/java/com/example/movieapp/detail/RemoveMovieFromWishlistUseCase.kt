package com.example.movieapp.detail

import com.example.movieapp.core.network.repository.WishListRepository
import javax.inject.Inject

class RemoveMovieFromWishlistUseCase @Inject constructor(private val repository: WishListRepository) {
    suspend operator fun invoke(id: Int) = repository.removeMovieFromWishlist(id)
}