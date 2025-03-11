package com.example.movieapp.database.source

import com.example.movieapp.core.database.database.MovieDatabase
import com.example.movieapp.core.database.util.DatabaseTransactionProvider
import org.mockito.Mockito.mock

class FakeTransactionProvider : DatabaseTransactionProvider(
    mock(MovieDatabase::class.java)
) {
    override suspend fun <R> runWithTransaction(block: suspend () -> R): R {
        return block() // Directly execute the transaction block
    }
}
