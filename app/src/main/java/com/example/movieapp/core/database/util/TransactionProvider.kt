package com.example.movieapp.core.database.util

import androidx.room.withTransaction
import com.example.movieapp.core.database.database.MovieDatabase
import javax.inject.Inject

open class DatabaseTransactionProvider @Inject constructor(
    private val movieDatabase : MovieDatabase
) {
    open suspend fun <R> runWithTransaction(block: suspend () -> R) =
        movieDatabase.withTransaction(block)
}