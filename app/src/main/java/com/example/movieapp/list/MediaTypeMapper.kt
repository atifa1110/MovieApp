package com.example.movieapp.list

import androidx.annotation.StringRes
import com.example.movieapp.R
import com.example.movieapp.core.model.MediaType

@StringRes
internal fun MediaType.Common.asTitleResourceId() = mediaTypesTitleResources.getValue(this)

private val mediaTypesTitleResources = mapOf(
    MediaType.Common.Upcoming to R.string.upcoming_movies,
    MediaType.Common.TopRated to R.string.top_rated,
    MediaType.Common.Popular to R.string.most_popular,
    MediaType.Common.NowPlaying to R.string.now_playing,
    MediaType.Common.Discover to R.string.discover,
    MediaType.Common.Trending to R.string.trending
)