package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.CastModel
import com.example.movieapp.core.domain.CreditsModel
import com.example.movieapp.core.domain.CrewModel
import com.example.movieapp.core.model.Credits
import org.junit.Assert.assertEquals
import org.junit.Test

class CreditsMapperTest {

    @Test
    fun `CreditsModel asCredits maps correctly`() {
        val castModel = CastModel(
            id = 1,
            name = "Actor One",
            adult = false,
            character = "Hero",
            profilePath = "/actor1.jpg",
            castId = 0,
            creditId = "",
            gender = 0,
            knownForDepartment = "",
            order = 0, originalName = "",
            popularity = 0.0
        )

        val crewModel = CrewModel(
            id = 2,
            adult = false,
            creditId = "",
            department = "",
            gender = null,
            knownForDepartment = "",
            name = "Director One",
            originalName = "",
            popularity = 0.0,
            job = "Director",
            profilePath = "/director1.jpg"
        )

        val creditsModel = CreditsModel(
            cast = listOf(castModel),
            crew = listOf(crewModel)
        )

        val credits: Credits = creditsModel.asCredits()

        assertEquals(1, credits.cast.size)
        assertEquals(1, credits.crew.size)

        // Verify cast mapping
        val mappedCast = credits.cast.first()
        assertEquals(1, mappedCast.id)
        assertEquals("Actor One", mappedCast.name)
        assertEquals("Hero", mappedCast.character)
        assertEquals("/actor1.jpg", mappedCast.profilePath)

        // Verify crew mapping
        val mappedCrew = credits.crew.first()
        assertEquals(2, mappedCrew.id)
        assertEquals("Director One", mappedCrew.name)
        assertEquals("Director", mappedCrew.job)
        assertEquals("/director1.jpg", mappedCrew.profilePath)
    }

    @Test
    fun `CreditsModel asCredits handles empty lists`() {
        val creditsModel = CreditsModel(
            cast = emptyList(),
            crew = emptyList()
        )

        val credits: Credits = creditsModel.asCredits()

        assertEquals(0, credits.cast.size)
        assertEquals(0, credits.crew.size)
    }
}
