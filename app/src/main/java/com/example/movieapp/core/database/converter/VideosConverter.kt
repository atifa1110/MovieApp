package com.example.movieapp.core.database.converter

import androidx.room.TypeConverter
import com.example.movieapp.core.database.model.detailmovie.VideosEntity
import com.google.gson.Gson

internal class VideosConverter {

    @TypeConverter
    fun fromVideos(videos : VideosEntity): String {
        return Gson().toJson(videos)
    }

    @TypeConverter
    fun toVideos(json: String): VideosEntity {
        return Gson().fromJson(json, VideosEntity::class.java)
    }
}