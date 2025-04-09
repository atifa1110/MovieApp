package com.example.movieapp.core.data

import com.example.movieapp.core.database.model.detailmovie.VideoEntity
import com.example.movieapp.core.database.model.detailmovie.VideosEntity
import com.example.movieapp.core.network.response.movies.NetworkVideo
import com.example.movieapp.core.network.response.movies.NetworkVideos
import org.junit.Assert.assertEquals
import org.junit.Test

class VideoMappingTest {

    @Test
    fun `test NetworkVideos to VideosEntity mapping`() {
        val networkVideo = NetworkVideo(
            id = "123",
            language = "en",
            country = "US",
            key = "abc123",
            name = "Sample Video",
            official = true,
            publishedAt = "2024-03-31T12:00:00Z",
            site = "YouTube",
            size = 1080,
            type = "Trailer"
        )
        val networkVideos = NetworkVideos(results = listOf(networkVideo))

        val videosEntity = networkVideos.asVideoEntity()

        assertEquals(1, videosEntity.results.size)
        val videoEntity = videosEntity.results.first()
        assertEquals(networkVideo.id, videoEntity.id)
        assertEquals(networkVideo.language, videoEntity.language)
        assertEquals(networkVideo.country, videoEntity.country)
    }

    @Test
    fun `test VideosEntity to VideosModel mapping`() {
        val videoEntity = VideoEntity(
            id = "123",
            language = "en",
            country = "US",
            key = "abc123",
            name = "Sample Video",
            official = true,
            publishedAt = "2024-03-31T12:00:00Z",
            site = "YouTube",
            size = 1080,
            type = "Trailer"
        )
        val videosEntity = VideosEntity(results = listOf(videoEntity))

        val videosModel = videosEntity.asVideoModel()

        assertEquals(1, videosModel.results.size)
        val videoModel = videosModel.results.first()
        assertEquals(videoEntity.id, videoModel.id)
        assertEquals(videoEntity.language, videoModel.language)
        assertEquals(videoEntity.country, videoModel.country)
    }
}