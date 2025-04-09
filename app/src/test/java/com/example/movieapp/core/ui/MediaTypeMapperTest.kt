package com.example.movieapp.core.ui

import com.example.movieapp.core.model.MediaType
import com.example.movieapp.core.domain.MediaTypeModel
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaTypeMapperTest {

    @Test
    fun `test MediaType Movie asMediaTypeModel maps correctly`() {
        // Given: A MediaType.Movie
        val movieType = MediaType.Movie.Popular

        // When: Mapping occurs
        val result = movieType.asMediaTypeModel()

        // Then: Assert mapping is correct
        assertEquals(MediaTypeModel.Movie.Popular, result)
    }

    @Test
    fun `test MediaType TvShow asMediaTypeModel maps correctly`() {
        // Given: A MediaType.TvShow
        val tvShowType = MediaType.TvShow.Trending

        // When: Mapping occurs
        val result = tvShowType.asMediaTypeModel()

        // Then: Assert mapping is correct
        assertEquals(MediaTypeModel.TvShow.Trending, result)
    }

    @Test
    fun `test MediaType Common asMovieMediaType maps correctly`() {
        // Given: A MediaType.Common Movie type
        val commonType = MediaType.Common.Movie.NowPlaying

        // When: Mapping occurs
        val result = commonType.asMovieMediaType()

        // Then: Assert mapping is correct
        assertEquals(MediaType.Movie.NowPlaying, result)
    }

    @Test
    fun `test MediaType Common asTvShowMediaType maps correctly`() {
        // Given: A MediaType.Common TvShow type
        val commonType = MediaType.Common.TvShow.Popular

        // When: Mapping occurs
        val result = commonType.asTvShowMediaType()

        // Then: Assert mapping is correct
        assertEquals(MediaType.TvShow.Popular, result)
    }

}
