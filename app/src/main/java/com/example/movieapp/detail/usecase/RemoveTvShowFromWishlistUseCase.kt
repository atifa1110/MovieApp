package com.example.movieapp.detail.usecase

import com.example.movieapp.core.network.repository.WishListRepository
import javax.inject.Inject

class RemoveTvShowFromWishlistUseCase @Inject constructor(private val repository: WishListRepository) {
    suspend operator fun invoke(id: Int) = repository.removeTvShowFromWishlist(id)
}