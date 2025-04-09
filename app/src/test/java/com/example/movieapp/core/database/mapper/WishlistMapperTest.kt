package com.example.movieapp.core.database.mapper

import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.database.model.wishlist.WishlistEntity
import com.example.movieapp.core.database.util.MediaType
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.ImagesModel
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.domain.MovieDetailModel
import com.example.movieapp.core.domain.TvShowDetailModel
import com.example.movieapp.core.domain.VideosModel
import org.junit.Assert.*
import org.junit.Test

class WishlistMapperTest {

    @Test
    fun `WishlistEntity asWishlistModel returns correct WishlistModel`() {
        val entity = WishlistEntity(
            mediaType = MediaType.Wishlist.Movie,
            networkId = 123,
            title = "Inception",
            genreEntities = listOf(GenreEntity(1, "action")),
            rating = 8.8,
            posterPath = "/poster.jpg",
            isWishListed = true
        )

        val model = entity.asWishlistModel()

        assertEquals(123, model.id)
        assertEquals(MediaTypeModel.Wishlist.Movie, model.mediaType)
        assertEquals("Inception", model.title)
        assertEquals(8.8, model.rating, 0.0)
        assertTrue(model.isWishListed)
    }

    @Test
    fun `MediaType Wishlist asWishlistEntity correctly maps MovieDetailModel`() {
        val movieDetail = MovieDetailModel(
            id = 456, adult = false, backdropPath = null, budget = 0,
            genres = listOf(com.example.movieapp.core.domain.GenreModel.ACTION),
            homepage = null, imdbId = null, originalLanguage = "", originalTitle = "",
            overview = "", popularity = 0.0, posterPath = null,
            releaseDate = null, revenue = 0, runtime = null, status = "",
            tagline = null, title = "Interstellar", video = false, rating = 9.0,
            isWishListed = false, voteAverage = 0.0, voteCount = 0,
            credits = CreditsModel(emptyList(), emptyList()),
            videos = VideosModel(emptyList()),
            images = ImagesModel(emptyList(), emptyList())
        )

        val entity = MediaType.Wishlist.Movie.asWishlistEntity(movieDetail)

        assertEquals(456, entity.networkId)
        assertEquals(MediaType.Wishlist.Movie, entity.mediaType)
        assertEquals("Interstellar", entity.title)
        assertEquals(9.0, entity.rating, 0.0)
        assertFalse(entity.isWishListed)
    }

    @Test
    fun `MediaType Wishlist asWishlistEntity correctly maps TvShowDetailModel`() {
        val tvShowDetail = TvShowDetailModel(
            id = 789, name = "Breaking Bad", adult = false, backdropPath = "", episodeRunTime = emptyList(),
            firstAirDate = "", genres = listOf(com.example.movieapp.core.domain.GenreModel.ACTION), seasons = emptyList(), homepage = "",
            inProduction = false, languages =  emptyList(), lastAirDate = "", numberOfEpisodes = 0,
            numberOfSeasons = 0, originCountry = emptyList(), originalLanguage = "",
            originalName = "Breaking Bad", overview = "", popularity = 0.0, posterPath = "path/to/breakingbad",
            status = "" , tagline = "", type = "", voteAverage = 0.0, voteCount = 0,
            credits = CreditsModel(emptyList(), emptyList()), rating = 9.5, isWishListed = true
        )

        val entity = MediaType.Wishlist.TvShow.asWishlistEntity(tvShowDetail)

        assertEquals(789, entity.networkId)
        assertEquals(MediaType.Wishlist.TvShow, entity.mediaType)
        assertEquals("Breaking Bad", entity.title)
        assertEquals(9.5, entity.rating, 0.0)
        assertEquals("path/to/breakingbad", entity.posterPath)
        assertTrue(entity.isWishListed)
    }
}
