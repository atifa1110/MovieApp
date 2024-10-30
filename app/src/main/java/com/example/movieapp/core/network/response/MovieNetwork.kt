package com.example.movieapp.core.network.response

import com.example.movieapp.core.network.Constants
import com.example.movieapp.core.network.calculateStarRating
import com.example.movieapp.core.network.capitalizeFirstLetter
import com.example.movieapp.core.network.getDateCustomFormat
import com.example.movieapp.core.network.getPhoto
import com.example.movieapp.core.network.getTitle
import com.google.gson.annotations.SerializedName

data class MovieNetwork(
    @SerializedName(Constants.Fields.ADULT)
    val adult: Boolean? = null,
    @SerializedName(Constants.Fields.BACKDROP_PATH)
    val backdropPath: String? = null,
    @SerializedName(Constants.Fields.GENRE_IDS)
    val genreIds: List<Int>? = null,
    @SerializedName(Constants.Fields.ID)
    val id: Int? = null,
    @SerializedName(Constants.Fields.ORIGINAL_LANGUAGE)
    val originalLanguage: String? = null,
    @SerializedName(Constants.Fields.ORIGINAL_TITLE)
    val originalTitle: String? = null,
    @SerializedName(Constants.Fields.NAME)
    val name : String? = null,
    @SerializedName(Constants.Fields.ORIGINAL_NAME)
    val originalName: String? = null,
    @SerializedName(Constants.Fields.OVERVIEW)
    val overview: String? = null,
    @SerializedName(Constants.Fields.POPULARITY)
    val popularity: Double? = null,
    @SerializedName(Constants.Fields.POSTER_PATH)
    val posterPath: String? = null,
    @SerializedName(Constants.Fields.PROFILE_PATH)
    val profilePath: String? = null,
    @SerializedName(Constants.Fields.MEDIA_TYPE)
    val mediaType: String? = null,
    @SerializedName(Constants.Fields.RELEASE_DATE)
    val releaseDate: String? = null,
    @SerializedName(Constants.Fields.FIRST_AIR_DATE)
    val firstAirDate: String? = null,
    @SerializedName(Constants.Fields.TITLE)
    val title: String? = null,
    @SerializedName(Constants.Fields.VIDEO)
    val video: Boolean? = null,
    @SerializedName(Constants.Fields.VOTE_AVERAGE)
    val voteAverage: Double? = null,
    @SerializedName(Constants.Fields.VOTE_COUNT)
    val voteCount: Int? = null
)