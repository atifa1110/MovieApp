package com.example.movieapp.database.converter

import com.example.movieapp.core.database.converter.CreditsConverter
import com.example.movieapp.core.database.model.detailmovie.CastEntity
import com.example.movieapp.core.database.model.detailmovie.CreditsEntity
import com.example.movieapp.core.database.model.detailmovie.CrewEntity
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CreditsConverterTest {

    private val creditsConverter = CreditsConverter()
    private val gson = Gson()

    @Test
    fun `fromCredits - converts CreditsEntity to JSON`() {
        val credits = CreditsEntity(
            cast = listOf(CastEntity(
                id = 1, name = "Cast 1", adult = false, castId = 1, character = "",
                creditId = "", gender = 0, knownForDepartment ="",
                order = 0, originalName = "Cast 1", popularity = 0.0, profilePath = "")),
            crew = listOf(CrewEntity(
                id = 1, adult = false, creditId ="", department = "", gender = 0,
                job = "", knownForDepartment = "", name = "", originalName = "",
                popularity = 0.0, profilePath = "")
            )
        )

        val json = creditsConverter.fromCredits(credits)

        assertNotNull(json)
        assertEquals(gson.toJson(credits), json)
    }

    @Test
    fun `toCredits - converts JSON to CreditsEntity`() {
        val credits = CreditsEntity(
            cast = listOf(CastEntity(
                id = 1, name = "Cast 1", adult = false, castId = 1, character = "",
                creditId = "", gender = 0, knownForDepartment ="",
                order = 0, originalName = "Cast 1", popularity = 0.0, profilePath = "")),
            crew = listOf(CrewEntity(
                id = 1, adult = false, creditId ="", department = "", gender = 0,
                job = "", knownForDepartment = "", name = "", originalName = "",
                popularity = 0.0, profilePath = "")
            )
        )

        val json = gson.toJson(credits)

        val convertedCredits = creditsConverter.toCredits(json)

        assertNotNull(convertedCredits)
        assertEquals(credits, convertedCredits)
    }
}