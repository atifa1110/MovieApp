package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.MovieModel
import com.example.movieapp.core.domain.SearchModel
import com.example.movieapp.core.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieMapperTest {

    @Test
    fun `test MovieModel to Movie mapping`() {
        val movieModel = MovieModel(
            id = 1,
            title = "Inception",
            adult = false,
            overview = "A mind-bending thriller",
            releaseDate = "2010-07-16",
            genres = null,
            rating = 8.8,
            backdropPath = "/backdrop.jpg",
            posterPath = "/poster.jpg",
            profilePath = null,
            mediaType = "movie",
            runtime = 148
        )

        val movie = movieModel.asMovie()

        assertEquals(movieModel.id, movie.id)
        assertEquals(movieModel.title, movie.title)
        assertEquals(movieModel.adult, movie.adult)
        assertEquals(movieModel.overview, movie.overview)
        assertEquals(movieModel.releaseDate, movie.releaseDate)

    }

    @Test
    fun `test SearchModel to Movie mapping`() {
        val searchModel = SearchModel(
            id = 2,
            title = "Interstellar",
            adult = false,
            overview = "A journey beyond the stars",
            releaseDate = "2014-11-07",
            genres = null,
            rating = 8.6,
            backdropPath = "/interstellar_backdrop.jpg",
            posterPath = "/interstellar_poster.jpg",
            profilePath = null,
            mediaType = "movie",
            runtime = 169,
            timestamp = System.currentTimeMillis()
        )

        val movie = searchModel.asMovie()

        assertEquals(searchModel.id, movie.id)
        assertEquals(searchModel.title, movie.title)
        assertEquals(searchModel.adult, movie.adult)
        assertEquals(searchModel.overview, movie.overview)
        assertEquals(searchModel.releaseDate, movie.releaseDate)
    }

    @Test
    fun `test Movie to SearchModel mapping`() {
        val movie = Movie(
            id = 3,
            title = "The Dark Knight",
            adult = false,
            overview = "A thrilling superhero movie",
            releaseDate = "2008-07-18",
            genres = null,
            rating = 9.0,
            backdropPath = "/dark_knight_backdrop.jpg",
            posterPath = "/dark_knight_poster.jpg",
            profilePath = null,
            mediaType = "movie",
            runtime = 152
        )

        val searchModel = movie.asSearchModel()

        assertEquals(movie.id, searchModel.id)
        assertEquals(movie.title, searchModel.title)
        assertEquals(movie.adult, searchModel.adult)
        assertEquals(movie.overview, searchModel.overview)
        assertEquals(movie.releaseDate, searchModel.releaseDate)
    }
}
