package com.example.movieapp.list.usecase

import androidx.paging.PagingData
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTvPagingUseCase @Inject constructor(
    private val tvShowRepository : TvShowRepository
) {
    operator fun invoke(mediaTypeModel: MediaTypeModel.TvShow) : Flow<PagingData<TvShowModel>> {
        return tvShowRepository.getPagingByMediaType(mediaTypeModel)
    }
}