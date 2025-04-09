package com.example.movieapp.core.data

import com.example.movieapp.core.network.NetworkMediaType
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.MediaTypeModel
import org.junit.Assert.*
import org.junit.Test

class MediaTypeTest {

    @Test
    fun `Movie asNetworkMediaType returns correct mapping`() {
        assertEquals(NetworkMediaType.Movie["movie_upcoming"], MediaType.Movie.Upcoming.asNetworkMediaType())
        assertEquals(NetworkMediaType.Movie["movie_top_rated"], MediaType.Movie.TopRated.asNetworkMediaType())
        assertEquals(NetworkMediaType.Movie["movie_popular"], MediaType.Movie.Popular.asNetworkMediaType())
        assertEquals(NetworkMediaType.Movie["movie_now_playing"], MediaType.Movie.NowPlaying.asNetworkMediaType())
        assertEquals(NetworkMediaType.Movie["movie_trending"], MediaType.Movie.Trending.asNetworkMediaType())
    }

    @Test
    fun `TvShow asNetworkMediaType returns correct mapping`() {
        assertEquals(NetworkMediaType.TvShow["top_rated"], MediaType.TvShow.TopRated.asNetworkMediaType())
        assertEquals(NetworkMediaType.TvShow["popular"], MediaType.TvShow.Popular.asNetworkMediaType())
        assertEquals(NetworkMediaType.TvShow["now_playing"], MediaType.TvShow.NowPlaying.asNetworkMediaType())
        assertEquals(NetworkMediaType.TvShow["discover"], MediaType.TvShow.Discover.asNetworkMediaType())
        assertEquals(NetworkMediaType.TvShow["trending"], MediaType.TvShow.Trending.asNetworkMediaType())
    }

    @Test
    fun `Wishlist asMediaTypeModel returns correct model`() {
        assertEquals(MediaTypeModel.Wishlist.Movie, MediaType.Wishlist.Movie.asMediaTypeModel())
        assertEquals(MediaTypeModel.Wishlist.TvShow, MediaType.Wishlist.TvShow.asMediaTypeModel())
    }
}
