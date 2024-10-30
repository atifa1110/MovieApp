package com.example.movieapp.core.database.model.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.movieapp.core.database.util.Constants

@Entity(
    tableName = Constants.Tables.GENRES,
)
data class GenreEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.Fields.ID)
    val id: Int = 0,

    @ColumnInfo(name = Constants.Fields.NAME)
    val name: String,

//    @ColumnInfo(name = Constants.Fields.MOVIE_ID) // This is the foreign key column
//    val movieId: Int // Foreign key referencing MovieEntity's id
)