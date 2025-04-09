package com.example.movieapp.core.database.mapper

import com.example.movieapp.core.database.model.movie.GenreEntity
import com.example.movieapp.core.database.model.search.SearchEntity
import com.example.movieapp.core.domain.GenreModel
import com.example.movieapp.core.domain.SearchModel
import org.junit.Assert.*
import org.junit.Test

class SearchMapperTest {

    @Test
    fun `SearchEntity asSearchModel returns correct SearchModel`() {
        val entity = SearchEntity(
            id = 101,
            title = "Inception",
            overview = "A mind-bending thriller",
            popularity = 98.5,
            releaseDate = "2010-07-16",
            adult = false,
            genreEntities = listOf(GenreEntity(1, "action")),
            originalTitle = "Inception",
            originalLanguage = "en",
            voteAverage = 8.8,
            voteCount = 10000,
            posterPath = "path/to/poster",
            backdropPath = "path/to/backdrop",
            video = false,
            rating = 4.5,
            mediaType = "movie",
            runtime = 148,
            timestamp = 1612345678
        )

        val model = entity.asSearchModel()

        assertEquals(101, model.id)
        assertEquals("Inception", model.title)
        assertEquals("A mind-bending thriller", model.overview)
        assertEquals("2010-07-16", model.releaseDate)
        assertEquals(10000, model.voteCount)
        assertEquals("movie", model.mediaType)
        assertEquals(148, model.runtime)
    }

    @Test
    fun `SearchModel asSearchEntity returns correct SearchEntity`() {
        val model = SearchModel(
            id = 202,
            title = "Interstellar",
            overview = "A journey through space and time",
            popularity = 95.7,
            releaseDate = "2014-11-07",
            adult = false,
            genres = listOf(GenreModel.ACTION),
            originalTitle = "Interstellar",
            originalLanguage = "en",
            voteAverage = 9.0,
            voteCount = 12000,
            posterPath = "path/to/interstellar",
            backdropPath = "path/to/interstellar_backdrop",
            video = true,
            rating = 4.8,
            mediaType = "movie",
            runtime = 169,
            timestamp = 1612345689
        )

        val entity = model.asSearchEntity()

        assertEquals(202, entity.id)
        assertEquals("Interstellar", entity.title)
        assertEquals("A journey through space and time", entity.overview)
        assertEquals(95.7, entity.popularity, 0.0)
        assertFalse(entity.adult)
        assertEquals(9.0, entity.voteAverage, 0.0)
        assertEquals(12000, entity.voteCount)
        assertTrue(entity.video)
        assertEquals(4.8, entity.rating, 0.0)
        assertEquals("movie", entity.mediaType)
        assertEquals(169, entity.runtime)
        assertEquals(1612345689, entity.timestamp)
    }
}
