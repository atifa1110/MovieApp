package com.example.movieapp.core.network.response.movies

import com.example.movieapp.core.network.Constants
import com.example.movieapp.core.network.GenreNetwork
import com.google.gson.annotations.SerializedName

data class MovieDetailNetwork(
    @SerializedName(Constants.Fields.ID)
    val id: Int,

    @SerializedName(Constants.Fields.ADULT)
    val adult: Boolean,

    @SerializedName(Constants.Fields.BACKDROP_PATH)
    val backdropPath: String? = null,

    @SerializedName(Constants.Fields.BELONGS_TO_COLLECTION)
    val belongsToCollection: NetworkBelongsToCollection?= null,

    @SerializedName(Constants.Fields.BUDGET)
    val budget: Int? = null,

    @SerializedName(Constants.Fields.GENRES)
    val genres: List<GenreNetwork>? = null,

    @SerializedName(Constants.Fields.HOMEPAGE)
    val homepage: String? = null,

    @SerializedName(Constants.Fields.IMDB_ID)
    val imdbId: String? = null,

    @SerializedName(Constants.Fields.ORIGINAL_LANGUAGE)
    val originalLanguage: String? = null,

    @SerializedName(Constants.Fields.ORIGINAL_TITLE)
    val originalTitle: String? = null,

    @SerializedName(Constants.Fields.OVERVIEW)
    val overview: String? = null,

    @SerializedName(Constants.Fields.POPULARITY)
    val popularity: Double?= null,

    @SerializedName(Constants.Fields.POSTER_PATH)
    val posterPath: String? = null,

    @SerializedName(Constants.Fields.PRODUCTION_COMPANIES)
    val productionCompanies: List<NetworkProductionCompany>? = null,

    @SerializedName(Constants.Fields.PRODUCTION_COUNTRIES)
    val productionCountries: List<NetworkProductionCountry>? = null,

    @SerializedName(Constants.Fields.RELEASE_DATE)
    val releaseDate: String? = null,

    @SerializedName(Constants.Fields.REVENUE)
    val revenue: Long?= null,

    @SerializedName(Constants.Fields.RUNTIME)
    val runtime: Int?= null,

    @SerializedName(Constants.Fields.SPOKEN_LANGUAGES)
    val spokenLanguages: List<NetworkSpokenLanguage>? = null,

    @SerializedName(Constants.Fields.STATUS)
    val status: String? = null,

    @SerializedName(Constants.Fields.TAGLINE)
    val tagline: String?= null,

    @SerializedName(Constants.Fields.TITLE)
    val title: String?= null,

    @SerializedName(Constants.Fields.VIDEO)
    val video: Boolean?= null,

    @SerializedName(Constants.Fields.VOTE_AVERAGE)
    val voteAverage: Double?= null,

    @SerializedName(Constants.Fields.VOTE_COUNT)
    val voteCount: Int?= null,

    @SerializedName(Constants.Fields.CREDITS)
    val credits: NetworkCredits? ?= null,

    @SerializedName(Constants.Fields.IMAGES)
    val images: NetworkImages? = null,

    @SerializedName(Constants.Fields.VIDEOS)
    val videos: NetworkVideos? = null,
)

data class NetworkVideos(
    @SerializedName(Constants.Fields.RESULTS)
    val results : List<NetworkVideo>
)

data class NetworkVideo(
    @SerializedName(Constants.Fields.ID)
    val id: String,
    @SerializedName(Constants.Fields.ISO6391)
    val language: String,
    @SerializedName(Constants.Fields.ISO31661)
    val country: String,
    @SerializedName(Constants.Fields.KEY)
    val key: String, // Video key used for URLs (e.g., YouTube)
    @SerializedName(Constants.Fields.NAME)
    val name: String, // Name of the video (e.g., "Trailer")
    @SerializedName(Constants.Fields.SITE)
    val site: String, // Site hosting the video (e.g., "YouTube")
    @SerializedName(Constants.Fields.SIZE)
    val size: Int, // Resolution size (e.g., 1080)
    @SerializedName(Constants.Fields.TYPE)
    val type: String, // Type of video (e.g., "Trailer", "Teaser")
    @SerializedName(Constants.Fields.OFFICIAL)
    val official: Boolean, // Whether it's an official video
    @SerializedName(Constants.Fields.PUBLISHED)
    val publishedAt: String // Publication date
)

data class NetworkImages(
    @SerializedName(Constants.Fields.BACKDROPS)
    val backdrops: List<NetworkImage>,
    @SerializedName(Constants.Fields.POSTERS)
    val posters : List<NetworkImage>
)

data class NetworkImage(
    @SerializedName(Constants.Fields.ASPECT_RATIO)
    val aspectRatio : Double,
    @SerializedName(Constants.Fields.HEIGHT)
    val height : Int,
    @SerializedName(Constants.Fields.WIDTH)
    val width : Int,
    @SerializedName(Constants.Fields.FILE_PATH)
    val filePath : String
)

data class NetworkSpokenLanguage(
    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.ENGLISH_NAME)
    val englishName: String,

    @SerializedName(Constants.Fields.ISO6391)
    val iso: String
)

data class NetworkBelongsToCollection(
    @SerializedName(Constants.Fields.ID)
    val id: Int,

    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.POSTER_PATH)
    val posterPath: String?,

    @SerializedName(Constants.Fields.BACKDROP_PATH)
    val backdropPath: String?
)

data class NetworkProductionCompany(
    @SerializedName(Constants.Fields.ID)
    val id: Int,

    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.LOGO_PATH)
    val logoPath: String?,

    @SerializedName(Constants.Fields.ORIGIN_COUNTRY)
    val originCountry: String
)


data class NetworkProductionCountry(
    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.ISO31661)
    val iso: String
)

data class NetworkCredits(
    @SerializedName(Constants.Fields.CAST)
    val cast: List<NetworkCast>,

    @SerializedName(Constants.Fields.CREW)
    val crew: List<NetworkCrew>
)

data class NetworkCast(
    @SerializedName(Constants.Fields.ID)
    val id: Int,

    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.ADULT)
    val adult: Boolean,

    @SerializedName(Constants.Fields.CAST_ID)
    val castId: Int?,

    @SerializedName(Constants.Fields.CHARACTER)
    val character: String,

    @SerializedName(Constants.Fields.CREDIT_ID)
    val creditId: String,

    @SerializedName(Constants.Fields.GENDER)
    val gender: Int?,

    @SerializedName(Constants.Fields.KNOWN_FOR_DEPARTMENT)
    val knownForDepartment: String,

    @SerializedName(Constants.Fields.ORDER)
    val order: Int,

    @SerializedName(Constants.Fields.ORIGINAL_NAME)
    val originalName: String,

    @SerializedName(Constants.Fields.POPULARITY)
    val popularity: Double,

    @SerializedName(Constants.Fields.PROFILE_PATH)
    val profilePath: String?
)

data class NetworkCrew(
    @SerializedName(Constants.Fields.ID)
    val id: Int,

    @SerializedName(Constants.Fields.ADULT)
    val adult: Boolean,

    @SerializedName(Constants.Fields.CREDIT_ID)
    val creditId: String,

    @SerializedName(Constants.Fields.DEPARTMENT)
    val department: String,

    @SerializedName(Constants.Fields.GENDER)
    val gender: Int?,

    @SerializedName(Constants.Fields.JOB)
    val job: String,

    @SerializedName(Constants.Fields.KNOWN_FOR_DEPARTMENT)
    val knownForDepartment: String,

    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.ORIGINAL_NAME)
    val originalName: String,

    @SerializedName(Constants.Fields.POPULARITY)
    val popularity: Double,

    @SerializedName(Constants.Fields.PROFILE_PATH)
    val profilePath: String?
)