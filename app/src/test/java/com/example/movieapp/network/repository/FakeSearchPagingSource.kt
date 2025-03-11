package com.example.movieapp.network.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.core.domain.MovieModel

class FakeSearchPagingSource(
    private val query: String,
    private val fakeData: List<MovieModel>
) : PagingSource<Int, MovieModel>() {

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        return try {
            // Simulating loading based on page
            val currentPage = params.key ?: 1

            // Mock data for the current page
            val data = fakeData

            // Simulate end of pagination when no more data is available
            val endOfPaginationReached = data.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            // Return the page result
            LoadResult.Page(
                data = data,
                prevKey = prevPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
