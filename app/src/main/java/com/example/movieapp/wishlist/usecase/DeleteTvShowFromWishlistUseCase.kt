package com.example.movieapp.wishlist.usecase

import com.example.movieapp.core.network.repository.WishListRepository
import javax.inject.Inject

class DeleteTvShowFromWishlistUseCase @Inject constructor(
    private val repository: WishListRepository
) {
    suspend operator fun invoke(id: Int) =
        repository.removeTvShowFromWishlist(id)
}