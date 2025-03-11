package com.example.movieapp.detail.usecase

import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.network.repository.WishListRepository
import javax.inject.Inject

class AddTvShowToWishlistUseCase @Inject constructor(
    private val repository: WishListRepository
) {
    suspend operator fun invoke(tvShow : TvShowDetailModel) = repository.addTvShowToWishlist(tvShow)
}