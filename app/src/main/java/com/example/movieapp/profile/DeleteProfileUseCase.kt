package com.example.movieapp.profile

import com.example.movieapp.core.network.repository.StorageRepository
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteProfileUseCase @Inject constructor(
    val storageRepository: StorageRepository
) {
    operator fun invoke(imageUrl : String) : Flow<CinemaxResponse<String>> {
        return storageRepository.deleteProfileFromStorage(imageUrl)
    }
}