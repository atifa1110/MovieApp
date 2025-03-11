package com.example.movieapp.core.network.repository

import androidx.paging.PagingData
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow

interface TvShowDetailRepository {
    fun getDetailTvShow(id: Int) : Flow<CinemaxResponse<TvShowDetailModel?>>
}