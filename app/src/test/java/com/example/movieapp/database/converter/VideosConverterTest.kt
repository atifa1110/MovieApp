package com.example.movieapp.database.converter

import com.example.movieapp.core.database.converter.VideosConverter
import com.example.movieapp.core.database.model.detailmovie.VideoEntity
import com.example.movieapp.core.database.model.detailmovie.VideosEntity
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class VideosConverterTest {

    private val videosConverter = VideosConverter()
    private val gson = Gson()

    @Test
    fun `fromVideos - converts VideosEntity to JSON`() {
        val videos = VideosEntity(
            results = listOf(
                VideoEntity(id = "1", language = "", country = "",key = "videoKey1", name = "Video 1",
                    site = "", size = 0, type = "",official = false, publishedAt = ""),
                VideoEntity(id = "2", language = "", country = "",key = "videoKey2", name = "Video 2",
                    site = "", size = 0, type = "",official = false, publishedAt = ""),
            )
        )

        val json = videosConverter.fromVideos(videos)
        assertNotNull(json)
        assertEquals(gson.toJson(videos), json)
    }

    @Test
    fun `toVideos - converts JSON to VideosEntity`() {
        val videos = VideosEntity(
            results = listOf(
                VideoEntity(id = "1", language = "", country = "",key = "videoKey1", name = "Video 1",
                    site = "", size = 0, type = "",official = false, publishedAt = ""),
                VideoEntity(id = "2", language = "", country = "",key = "videoKey2", name = "Video 2",
                    site = "", size = 0, type = "",official = false, publishedAt = ""),
            )
        )

        val json = gson.toJson(videos)
        val convertedVideos = videosConverter.toVideos(json)
        assertNotNull(convertedVideos)
        assertEquals(videos, convertedVideos)
    }
}