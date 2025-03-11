package com.example.movieapp.network.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.core.database.model.movie.MovieEntity


class FakeMoviePagingSource(
    private val movies: List<MovieEntity>
) : PagingSource<Int, MovieEntity>() {
    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        return LoadResult.Page(
            data = movies, // Pastikan `movies` tidak kosong!
            prevKey = null,
            nextKey = null
        )
    }
}
