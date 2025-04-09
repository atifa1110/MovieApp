package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailmovie.CastEntity
import com.example.movieapp.core.database.model.detailmovie.CreditsEntity
import com.example.movieapp.core.database.model.detailmovie.CrewEntity
import com.example.movieapp.core.network.asImageURL
import com.example.movieapp.core.network.response.movies.NetworkCast
import com.example.movieapp.core.network.response.movies.NetworkCredits
import com.example.movieapp.core.network.response.movies.NetworkCrew
import org.junit.Assert.assertEquals
import org.junit.Test

class CreditsMapperTest {
    @Test
    fun `test NetworkCast to CastEntity mapping`() {
        val networkCast = NetworkCast(
            id = 1,
            name = "Actor Name",
            adult = false,
            castId = 101,
            character = "Main Character",
            creditId = "credit123",
            gender = 1,
            knownForDepartment = "Acting",
            order = 2,
            originalName = "Original Actor Name",
            popularity = 9.5,
            profilePath = "/image.jpg"
        )

        val castEntity = networkCast.asCast()

        assertEquals(networkCast.id, castEntity.id)
        assertEquals(networkCast.name, castEntity.name)
        assertEquals(networkCast.character, castEntity.character)
        assertEquals(networkCast.profilePath?.asImageURL(), castEntity.profilePath)
    }

    @Test
    fun `test NetworkCrew to CrewEntity mapping`() {
        val networkCrew = NetworkCrew(
            id = 2,
            name = "Director Name",
            adult = false,
            creditId = "credit456",
            department = "Directing",
            gender = 1,
            job = "Director",
            knownForDepartment = "Directing",
            originalName = "Original Director Name",
            popularity = 8.7,
            profilePath = "/crew.jpg"
        )

        val crewEntity = networkCrew.asCrew()

        assertEquals(networkCrew.id, crewEntity.id)
        assertEquals(networkCrew.name, crewEntity.name)
        assertEquals(networkCrew.department, crewEntity.department)
        assertEquals(networkCrew.profilePath?.asImageURL(), crewEntity.profilePath)
    }

    @Test
    fun `test NetworkCredits to CreditsEntity mapping`() {
        val networkCredits = NetworkCredits(
            cast = listOf(
                NetworkCast(1, "Actor A", false, 101, "Hero", "c123", 1, "Acting", 1, "Original Actor A", 8.9, "/actorA.jpg")
            ),
            crew = listOf(
                NetworkCrew(
                    id = 2, name = "Director B", adult = false, creditId = "credit456", department = "Directing", gender = 1,
                    job = "Director", knownForDepartment = "Directing", originalName = "Original Director Name", popularity = 8.7, profilePath = "/crew.jpg"
                )
            )
        )

        val creditsEntity = networkCredits.asCredits()

        assertEquals(1, creditsEntity.cast.size)
        assertEquals("Actor A", creditsEntity.cast[0].name)
        assertEquals("Hero", creditsEntity.cast[0].character)

        assertEquals(1, creditsEntity.crew.size)
        assertEquals("Director B", creditsEntity.crew[0].name)
        assertEquals("Director", creditsEntity.crew[0].job)
    }

    @Test
    fun `test CreditsEntity to CreditsModel mapping`() {
        val creditsEntity = CreditsEntity(
            cast = listOf(
                CastEntity(1, "Actor A", false, 101, "Hero", "c123", 1, "Acting", 1, "Original Actor A", 8.9, "/actorA.jpg")
            ),
            crew = listOf(
                CrewEntity(2, false, "c456", "Directing", 1, "Director", "Directing", "Director B", "Original Director B", 9.2, "/directorB.jpg")
            )
        )

        val creditsModel = creditsEntity.asCreditsModel()

        assertEquals(1, creditsModel.cast.size)
        assertEquals("Actor A", creditsModel.cast[0].name)

        assertEquals(1, creditsModel.crew.size)
        assertEquals("Director B", creditsModel.crew[0].name)
    }
}