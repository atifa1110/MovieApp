package com.example.movieapp.detail.usecase

import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.network.repository.TvShowDetailRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailTvShowUseCase @Inject constructor(
    private val detailRepository : TvShowDetailRepository
) {
    operator fun invoke(id : Int): Flow<CinemaxResponse<TvShowDetailModel?>> {
        return detailRepository.getDetailTvShow(id)
    }
}