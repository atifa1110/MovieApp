package com.example.movieapp.core.network.repository

import androidx.paging.PagingData
import com.example.movieapp.core.database.mapper.GenreModel
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
interface MovieRepository {
    fun genreMovie(): Flow<CinemaxResponse<List<GenreModel>>>
    fun searchMovie(query: String) : Flow<PagingData<MovieModel>>
    fun getByMediaTypeGenre(mediaTypeModel: MediaTypeModel.Movie): Flow<CinemaxResponse<List<MovieModel>>>
    fun getByMediaType(mediaTypeModel: MediaTypeModel.Movie): Flow<CinemaxResponse<List<MovieModel>>>
    fun getPagingByMediaType(mediaTypeModel: MediaTypeModel.Movie): Flow<PagingData<MovieModel>>
}