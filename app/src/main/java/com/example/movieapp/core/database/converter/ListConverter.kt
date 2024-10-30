package com.example.movieapp.core.database.converter

import androidx.room.TypeConverter
import com.example.movieapp.core.database.model.movie.Genre
import com.example.movieapp.core.database.model.movie.GenreEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal class ListConverter {

    @TypeConverter
    fun fromGenreIntList(genreEntities: List<Int>?): String? {
        return if (genreEntities == null) null else Gson().toJson(genreEntities)
    }

    @TypeConverter
    fun toGenreIntList(genresString: String?): List<Int>? {
        if (genresString == null) return null
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(genresString, listType)
    }

    @TypeConverter
    fun fromGenreList(genreEntities: List<Genre>?): String? {
        return if (genreEntities == null) null else Gson().toJson(genreEntities)
    }

    @TypeConverter
    fun toGenreList(genresString: String?): List<Genre>? {
        if (genresString == null) return null
        val listType = object : TypeToken<List<Genre>>() {}.type
        return Gson().fromJson(genresString, listType)
    }

    @TypeConverter
    fun fromGenreEntityList(genreEntities: List<GenreEntity>?): String? {
        return if (genreEntities == null) null else Gson().toJson(genreEntities)
    }

    @TypeConverter
    fun toGenreEntityList(genresString: String?): List<GenreEntity>? {
        if (genresString == null) return null
        val listType = object : TypeToken<List<GenreEntity>>() {}.type
        return Gson().fromJson(genresString, listType)
    }
}