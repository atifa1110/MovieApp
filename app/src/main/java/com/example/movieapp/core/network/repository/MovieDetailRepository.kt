package com.example.movieapp.core.network.repository

import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {
    fun getDetailMovie(id: Int) : Flow<CinemaxResponse<MovieDetailModel?>>
}