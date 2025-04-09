package com.example.movieapp.core.ui

import com.example.movieapp.core.domain.VideoModel
import com.example.movieapp.core.domain.VideosModel
import com.example.movieapp.core.model.Video
import org.junit.Assert.assertEquals
import org.junit.Test

class VideosMapperTest {

    @Test
    fun `asVideos correctly maps VideosModel to Videos`() {
        // Given
        val videoModel1 = VideoModel(
            id = "vid123",
            country = "US",
            language = "en",
            key = "youtube_key_123",
            name = "Official Trailer",
            official = true,
            publishedAt = "2025-04-01T10:00:00Z",
            site = "YouTube",
            size = 1080,
            type = "Trailer"
        )

        val videoModel2 = VideoModel(
            id = "vid124",
            country = "KR",
            language = "ko",
            key = "youtube_key_456",
            name = "Teaser",
            official = false,
            publishedAt = "2025-04-02T12:00:00Z",
            site = "YouTube",
            size = 720,
            type = "Teaser"
        )

        val videosModel = VideosModel(results = listOf(videoModel1, videoModel2))

        // When
        val result = videosModel.asVideos()

        // Then
        assertEquals(2, result.results.size)

        val video1 = result.results[0]
        assertEquals("vid123", video1.id)
        assertEquals("US", video1.country)
        assertEquals("en", video1.language)

        val video2 = result.results[1]
        assertEquals("vid124", video2.id)
        assertEquals("KR", video2.country)
        assertEquals("ko", video2.language)

    }
}
