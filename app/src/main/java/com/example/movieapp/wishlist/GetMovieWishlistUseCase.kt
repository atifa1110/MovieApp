package com.example.movieapp.wishlist

import com.example.movieapp.core.domain.WishlistModel
import com.example.movieapp.core.network.repository.WishListRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishListRepository,
) {
    operator fun invoke() : Flow<CinemaxResponse<List<WishlistModel>>> {
        return wishlistRepository.getMovies()
    }
}