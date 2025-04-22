package com.example.movieapp.core.database.converter

import androidx.room.TypeConverter
import com.example.movieapp.core.database.model.detailMovie.CreditsEntity
import com.google.gson.Gson

internal class CreditsConverter {

    @TypeConverter
    fun fromCredits(credits: CreditsEntity): String {
        return Gson().toJson(credits)
    }

    @TypeConverter
    fun toCredits(json: String): CreditsEntity {
        return Gson().fromJson(json, CreditsEntity::class.java)
    }
}