package com.example.movieapp.core.network.repository

import android.net.Uri
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    fun uploadImageUri(imageUri: Uri): Flow<CinemaxResponse<String>>

    fun removeProfileImage(): Flow<CinemaxResponse<String>>

    fun deleteProfileFromStorage(imageUrl: String) : Flow<CinemaxResponse<String>>
}