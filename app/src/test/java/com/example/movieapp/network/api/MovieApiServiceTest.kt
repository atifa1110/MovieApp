package com.example.movieapp.network.api

import com.example.movieapp.core.network.GenreNetwork
import com.example.movieapp.core.network.api.MovieApiService
import com.example.movieapp.core.network.response.movies.MovieDetailNetwork
import com.example.movieapp.core.network.response.movies.MovieNetwork
import com.example.movieapp.core.network.response.movies.MovieResponse
import com.example.movieapp.core.network.response.movies.NetworkBelongsToCollection
import com.example.movieapp.core.network.response.movies.NetworkCredits
import com.example.movieapp.core.network.response.movies.NetworkImages
import com.example.movieapp.core.network.response.movies.NetworkProductionCompany
import com.example.movieapp.core.network.response.movies.NetworkSpokenLanguage
import com.example.movieapp.core.network.response.movies.NetworkVideos
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class MovieApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: MovieApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val client = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(MovieApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test search all service`() {
        // Load JSON from resources
        val mock = File("src/test/resources/movies/Search.json").readText()
        println("Loaded JSON: $mock")
        val mockResponse = MockResponse()
            .setBody(mock)
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val actual = apiService.getSearchAll("harry potter",0).body()
            val expected = MovieResponse(
                1, listOf(MovieNetwork(
                    backdropPath = "/5jkE2SzR5uR2egEb1rRhF22JyWN.jpg",
                    id = 671,
                    title = "Harry Potter and the Philosopher's Stone",
                    originalTitle = "Harry Potter and the Philosopher's Stone",
                    overview = "",
                    posterPath = "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg",
                    mediaType = "movie",
                    adult = false,
                    originalLanguage = "en",
                    genreIds = listOf(12,14),
                    popularity =  234.43,
                    releaseDate = "2001-11-16",
                    video = false,
                    voteAverage = 7.9,
                    voteCount = 27593)
                ),2,30
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test details service`() {
        // Load JSON from resources
        val mock = File("src/test/resources/movies/Details.json").readText()
        println("Loaded JSON: $mock")
        val mockResponse = MockResponse()
            .setBody(mock)
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val actual = apiService.getDetailsById(671).body()
            val expected = MovieDetailNetwork(
                id = 671,
                adult = false,
                backdropPath = "/5jkE2SzR5uR2egEb1rRhF22JyWN.jpg",
                belongsToCollection = NetworkBelongsToCollection(
                    id = 1241,
                    name = "Harry Potter Collection",
                    posterPath = "/s4hXqX1VyWMc2ctJRuNBDB7YNJ3.jpg",
                    backdropPath = "/xN6SBJVG8jqqKQrgxthn3J2m49S.jpg"
                ),
                budget = 125000000,
                genres = listOf(
                    GenreNetwork(id = 12, name = "Adventure"),
                    GenreNetwork(id = 14, name = "Fantasy")
                ),
                homepage = "https://www.warnerbros.com/movies/harry-potter-and-sorcerers-stone/",
                imdbId = "tt0241527",
                originalLanguage = "en",
                originalTitle = "Harry Potter and the Philosopher's Stone",
                overview = "Harry Potter has lived under the stairs at his aunt and uncle's house his whole life. But on his 11th birthday, he learns he's a powerful wizard—with a place waiting for him at the Hogwarts School of Witchcraft and Wizardry. As he learns to harness his newfound powers with the help of the school's kindly headmaster, Harry uncovers the truth about his parents' deaths—and about the villain who's to blame.",
                popularity = 239.401,
                posterPath = "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg",
                productionCompanies = listOf(
                    NetworkProductionCompany(
                        id = 174,
                        logoPath = "/zhD3hhtKB5qyv7ZeL4uLpNxgMVU.png",
                        name = "Warner Bros. Pictures",
                        originCountry = "US"
                    ),
                ),
                productionCountries = emptyList(),
                releaseDate = "2001-11-16",
                revenue = 976475550L,
                runtime = 152,
                spokenLanguages = listOf(
                    NetworkSpokenLanguage(
                        englishName = "English",
                        iso = "en",
                        name = "English"
                    )
                ),
                status = "Released",
                tagline = "Let the magic begin.",
                title = "Harry Potter and the Philosopher's Stone",
                video = false,
                voteAverage = 7.907,
                voteCount = 27620,
                credits = NetworkCredits(emptyList(), emptyList()),
                images = NetworkImages(emptyList(), emptyList()),
                videos = NetworkVideos(emptyList())
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test now playing service`() {
        // Load JSON from resources
        val mock = File("src/test/resources/movies/NowPlaying.json").readText()
        println("Loaded JSON: $mock")
        val mockResponse = MockResponse()
            .setBody(mock)
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val actual = apiService.getNowPlayingMovie().body()
            val expected = MovieResponse(
                1, listOf(MovieNetwork(
                    backdropPath = "/5jkE2SzR5uR2egEb1rRhF22JyWN.jpg",
                    id = 671,
                    title = "Harry Potter and the Philosopher's Stone",
                    originalTitle = "Harry Potter and the Philosopher's Stone",
                    overview = "",
                    posterPath = "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg",
                    mediaType = "movie",
                    adult = false,
                    originalLanguage = "en",
                    genreIds = listOf(12,14),
                    popularity =  234.43,
                    releaseDate = "2001-11-16",
                    video = false,
                    voteAverage = 7.9,
                    voteCount = 27593)
                ),2,30
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test popular service`() {
        // Load JSON from resources
        val mock = File("src/test/resources/movies/Popular.json").readText()
        println("Loaded JSON: $mock")
        val mockResponse = MockResponse()
            .setBody(mock)
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val actual = apiService.getPopularMovie().body()
            val expected = MovieResponse(
                1, listOf(MovieNetwork(
                    backdropPath = "/5jkE2SzR5uR2egEb1rRhF22JyWN.jpg",
                    id = 671,
                    title = "Harry Potter and the Philosopher's Stone",
                    originalTitle = "Harry Potter and the Philosopher's Stone",
                    overview = "",
                    posterPath = "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg",
                    mediaType = "movie",
                    adult = false,
                    originalLanguage = "en",
                    genreIds = listOf(12,14),
                    popularity =  234.43,
                    releaseDate = "2001-11-16",
                    video = false,
                    voteAverage = 7.9,
                    voteCount = 27593)
                ),2,30
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test top rated service`() {
        // Load JSON from resources
        val mock = File("src/test/resources/movies/TopRated.json").readText()
        println("Loaded JSON: $mock")
        val mockResponse = MockResponse()
            .setBody(mock)
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val actual = apiService.getTopRatedMovie().body()
            val expected = MovieResponse(
                1, listOf(MovieNetwork(
                    backdropPath = "/5jkE2SzR5uR2egEb1rRhF22JyWN.jpg",
                    id = 671,
                    title = "Harry Potter and the Philosopher's Stone",
                    originalTitle = "Harry Potter and the Philosopher's Stone",
                    overview = "",
                    posterPath = "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg",
                    mediaType = "movie",
                    adult = false,
                    originalLanguage = "en",
                    genreIds = listOf(12,14),
                    popularity =  234.43,
                    releaseDate = "2001-11-16",
                    video = false,
                    voteAverage = 7.9,
                    voteCount = 27593)
                ),2,30
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test trending service`() {
        // Load JSON from resources
        val mock = File("src/test/resources/movies/Trending.json").readText()
        println("Loaded JSON: $mock")
        val mockResponse = MockResponse()
            .setBody(mock)
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val actual = apiService.getTrendingMovie().body()
            val expected = MovieResponse(
                1, listOf(MovieNetwork(
                    backdropPath = "/5jkE2SzR5uR2egEb1rRhF22JyWN.jpg",
                    id = 671,
                    title = "Harry Potter and the Philosopher's Stone",
                    originalTitle = "Harry Potter and the Philosopher's Stone",
                    overview = "",
                    posterPath = "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg",
                    mediaType = "movie",
                    adult = false,
                    originalLanguage = "en",
                    genreIds = listOf(12,14),
                    popularity =  234.43,
                    releaseDate = "2001-11-16",
                    video = false,
                    voteAverage = 7.9,
                    voteCount = 27593)
                ),2,30
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test upcoming service`() {
        // Load JSON from resources
        val mock = File("src/test/resources/movies/Upcoming.json").readText()
        println("Loaded JSON: $mock")
        val mockResponse = MockResponse()
            .setBody(mock)
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val actual = apiService.getUpcomingMovie().body()
            val expected = MovieResponse(
                1, listOf(MovieNetwork(
                    backdropPath = "/5jkE2SzR5uR2egEb1rRhF22JyWN.jpg",
                    id = 671,
                    title = "Harry Potter and the Philosopher's Stone",
                    originalTitle = "Harry Potter and the Philosopher's Stone",
                    overview = "",
                    posterPath = "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg",
                    mediaType = "movie",
                    adult = false,
                    originalLanguage = "en",
                    genreIds = listOf(12,14),
                    popularity =  234.43,
                    releaseDate = "2001-11-16",
                    video = false,
                    voteAverage = 7.9,
                    voteCount = 27593)
                ),2,30
            )
            assertEquals(expected, actual)
        }
    }
}