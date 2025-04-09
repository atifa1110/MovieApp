package com.example.movieapp.viewmodel.search

import androidx.paging.PagingSource
import androidx.paging.PagingState

class TestPagingSource<T : Any>(private val data: List<T>) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return LoadResult.Page(data, prevKey = null, nextKey = null)
    }
}
