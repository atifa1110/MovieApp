package com.example.movieapp.core.database.converter

import androidx.room.TypeConverter
import com.example.movieapp.core.database.model.detailmovie.ImagesEntity
import com.google.gson.Gson

internal class ImagesConverter {

    @TypeConverter
    fun fromImages(images: ImagesEntity): String {
        return Gson().toJson(images)
    }

    @TypeConverter
    fun toImages(json: String): ImagesEntity {
        return Gson().fromJson(json, ImagesEntity::class.java)
    }
}