package com.example.movieapp.home.usecase

import com.example.movieapp.core.database.mapper.GenreModel
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.repository.TvShowRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTvShowUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    operator fun invoke(mediaTypeModel: MediaTypeModel.TvShow): Flow<CinemaxResponse<List<TvShowModel>>> {
        return tvShowRepository.getByMediaType(mediaTypeModel)
    }
}