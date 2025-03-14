package com.example.movieapp.core.database.model.tv

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.core.database.model.movie.Genre
import com.example.movieapp.core.database.util.Constants
import com.example.movieapp.core.database.util.MediaType

@Entity(tableName = Constants.Tables.TV_SHOWS)
data class TvShowEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.Fields.ID)
    val id: Int = 0,

    @ColumnInfo(name = Constants.Fields.MEDIA_TYPE)
    val mediaType: MediaType.TvShow,

    @ColumnInfo(name = Constants.Fields.NETWORK_ID)
    val networkId: Int,

    @ColumnInfo(name = Constants.Fields.NAME)
    val name: String,

    @ColumnInfo(name = Constants.Fields.OVERVIEW)
    val overview: String,

    @ColumnInfo(name = Constants.Fields.POPULARITY)
    val popularity: Double,

    @ColumnInfo(name = Constants.Fields.FIRST_AIR_DATE)
    val firstAirDate: String,

    @ColumnInfo(name = Constants.Fields.GENRE_IDS)
    val genres: List<Genre>,

    @ColumnInfo(name = Constants.Fields.ORIGINAL_NAME)
    val originalName: String,

    @ColumnInfo(name = Constants.Fields.ORIGINAL_LANGUAGE)
    val originalLanguage: String,

    @ColumnInfo(name = Constants.Fields.ORIGIN_COUNTRY)
    val originCountry: List<String>,

    @ColumnInfo(name = Constants.Fields.VOTE_AVERAGE)
    val voteAverage: Double,

    @ColumnInfo(name = Constants.Fields.VOTE_COUNT)
    val voteCount: Int,

    @ColumnInfo(name = Constants.Fields.POSTER_PATH)
    val posterPath: String?,

    @ColumnInfo(name = Constants.Fields.BACKDROP_PATH)
    val backdropPath: String?,

    @ColumnInfo(name = Constants.Fields.RUNTIME)
    val runtime: Int,

    @ColumnInfo(name = Constants.Fields.RATING)
    val rating: Double
)