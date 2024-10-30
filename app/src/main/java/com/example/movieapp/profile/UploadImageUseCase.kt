package com.example.movieapp.profile

import android.net.Uri
import com.example.movieapp.core.network.repository.StorageRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    operator fun invoke(user: User, imageUri : Uri) : Flow<CinemaxResponse<String>> {
        return storageRepository.uploadImageAndSaveUri(user,imageUri)
    }
}