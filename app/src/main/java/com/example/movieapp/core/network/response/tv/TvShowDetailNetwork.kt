package com.example.movieapp.core.network.response.tv

import com.example.movieapp.core.network.Constants
import com.example.movieapp.core.network.GenreNetwork
import com.example.movieapp.core.network.response.movies.NetworkCredits
import com.example.movieapp.core.network.response.movies.NetworkProductionCompany
import com.example.movieapp.core.network.response.movies.NetworkProductionCountry
import com.example.movieapp.core.network.response.movies.NetworkSpokenLanguage
import com.google.gson.annotations.SerializedName

data class TvShowDetailNetwork (
    @SerializedName(Constants.Fields.ID)
    val id: Int,

    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.ADULT)
    val adult: Boolean,

    @SerializedName(Constants.Fields.BACKDROP_PATH)
    val backdropPath: String? = null,

    @SerializedName(Constants.Fields.CREATED_BY)
    val createdBy: List<NetworkCreatedBy>? = null,

    @SerializedName(Constants.Fields.CREDITS)
    val credits: NetworkCredits? = null,

    @SerializedName(Constants.Fields.EPISODE_RUN_TIME)
    val episodeRunTime: List<Int>? = null,

    @SerializedName(Constants.Fields.FIRST_AIR_DATE)
    val firstAirDate: String? = null,

    @SerializedName(Constants.Fields.GENRES)
    val genres: List<GenreNetwork>? = null,

    @SerializedName(Constants.Fields.HOMEPAGE)
    val homepage: String? = null,

    @SerializedName(Constants.Fields.IN_PRODUCTION)
    val inProduction: Boolean? = null,

    @SerializedName(Constants.Fields.LANGUAGES)
    val languages: List<String>? = null,

    @SerializedName(Constants.Fields.LAST_AIR_DATE)
    val lastAirDate: String? = null,

    @SerializedName(Constants.Fields.LAST_EPISODE_TO_AIR)
    val lastEpisodeToAir: NetworkEpisode? = null,

    @SerializedName(Constants.Fields.NETWORKS)
    val organizations: List<NetworkOrganization>? = null,

    @SerializedName(Constants.Fields.NEXT_EPISODE_TO_AIR)
    val nextEpisodeToAir: NetworkEpisode? = null,

    @SerializedName(Constants.Fields.NUMBER_OF_EPISODES)
    val numberOfEpisodes: Int? = null,

    @SerializedName(Constants.Fields.NUMBER_OF_SEASONS)
    val numberOfSeasons: Int? = null,

    @SerializedName(Constants.Fields.ORIGIN_COUNTRY)
    val originCountry: List<String>? = null,

    @SerializedName(Constants.Fields.ORIGINAL_LANGUAGE)
    val originalLanguage: String? = null,

    @SerializedName(Constants.Fields.ORIGINAL_NAME)
    val originalName: String? = null,

    @SerializedName(Constants.Fields.OVERVIEW)
    val overview: String? = null,

    @SerializedName(Constants.Fields.POPULARITY)
    val popularity: Double? = null,

    @SerializedName(Constants.Fields.POSTER_PATH)
    val posterPath: String? = null,

    @SerializedName(Constants.Fields.PRODUCTION_COMPANIES)
    val productionCompanies: List<NetworkProductionCompany>? = null,

    @SerializedName(Constants.Fields.PRODUCTION_COUNTRIES)
    val productionCountries: List<NetworkProductionCountry>? = null,

    @SerializedName(Constants.Fields.SEASONS)
    val seasons: List<NetworkSeason>? = null,

    @SerializedName(Constants.Fields.SPOKEN_LANGUAGES)
    val spokenLanguages: List<NetworkSpokenLanguage>? = null,

    @SerializedName(Constants.Fields.STATUS)
    val status: String? = null,

    @SerializedName(Constants.Fields.TAGLINE)
    val tagline: String? = null,

    @SerializedName(Constants.Fields.TYPE)
    val type: String? = null,

    @SerializedName(Constants.Fields.VOTE_AVERAGE)
    val voteAverage: Double? = null,

    @SerializedName(Constants.Fields.VOTE_COUNT)
    val voteCount: Int? = null
)

data class NetworkSeason(
    @SerializedName(Constants.Fields.ID)
    val id: Int,

    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.OVERVIEW)
    val overview: String,

    @SerializedName(Constants.Fields.AIR_DATE)
    val airDate: String?,

    @SerializedName(Constants.Fields.EPISODE_COUNT)
    val episodeCount: Int,

    @SerializedName(Constants.Fields.SEASON_NUMBER)
    val seasonNumber: Int,

    @SerializedName(Constants.Fields.POSTER_PATH)
    val posterPath: String?,

    @SerializedName(Constants.Fields.VOTE_AVERAGE)
    val voteAverage: Double
)

data class NetworkEpisode(
    @SerializedName(Constants.Fields.ID)
    val id: Int?,

    @SerializedName(Constants.Fields.NAME)
    val name: String?,

    @SerializedName(Constants.Fields.AIR_DATE)
    val airDate: String?,

    @SerializedName(Constants.Fields.EPISODE_NUMBER)
    val episodeNumber: Int?,

    @SerializedName(Constants.Fields.OVERVIEW)
    val overview: String?,

    @SerializedName(Constants.Fields.PRODUCTION_CODE)
    val productionCode: String?,

    @SerializedName(Constants.Fields.RUNTIME)
    val runtime: Int?,

    @SerializedName(Constants.Fields.SEASON_NUMBER)
    val seasonNumber: Int?,

    @SerializedName(Constants.Fields.SHOW_ID)
    val showId: Int?,

    @SerializedName(Constants.Fields.STILL_PATH)
    val stillPath: String?,

    @SerializedName(Constants.Fields.VOTE_AVERAGE)
    val voteAverage: Double?,

    @SerializedName(Constants.Fields.VOTE_COUNT)
    val voteCount: Int?
)

data class NetworkCreatedBy(
    @SerializedName(Constants.Fields.ID)
    val id: Int,

    @SerializedName(Constants.Fields.CREDIT_ID)
    val creditId: String,

    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.GENDER)
    val gender: Int,

    @SerializedName(Constants.Fields.PROFILE_PATH)
    val profilePath: String?
)

data class NetworkOrganization(
    @SerializedName(Constants.Fields.ID)
    val id: Int,

    @SerializedName(Constants.Fields.NAME)
    val name: String,

    @SerializedName(Constants.Fields.ORIGIN_COUNTRY)
    val originCountry: String,

    @SerializedName(Constants.Fields.LOGO_PATH)
    val logoPath: String?
)