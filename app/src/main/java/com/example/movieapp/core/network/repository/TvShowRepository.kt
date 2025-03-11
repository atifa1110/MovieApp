package com.example.movieapp.core.network.repository

import androidx.paging.PagingData
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.TvShowModel
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {

    fun getByMediaType(mediaTypeModel: MediaTypeModel.TvShow): Flow<CinemaxResponse<List<TvShowModel>>>
    fun getPagingByMediaType(mediaTypeModel: MediaTypeModel.TvShow): Flow<PagingData<TvShowModel>>
}