package com.example.movieapp.core.network.repository

import android.net.Uri
import com.example.movieapp.core.network.datasource.AuthNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val authNetworkDataSource: AuthNetworkDataSource
) : StorageRepository{

    override fun uploadImageUri(imageUri: Uri): Flow<CinemaxResponse<String>> = callbackFlow {
        trySend(CinemaxResponse.Loading) // Emit loading state

        val fileRef = authNetworkDataSource.uploadStorage()
        val uploadTask = fileRef.putFile(imageUri)

        // Listener untuk menangani sukses dan kegagalan
        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                trySend(CinemaxResponse.Success(uri.toString())) // Emit sukses dengan URL gambar
                close() // Tutup flow setelah berhasil
            }.addOnFailureListener {
                trySend(CinemaxResponse.Failure("Failed to get download URL"))
                close() // Tutup flow setelah gagal
            }
        }.addOnFailureListener {
            trySend(CinemaxResponse.Failure("Image upload failed"))
            close() // Tutup flow setelah gagal
        }

        awaitClose ()
    }


    override fun removeProfileImage(): Flow<CinemaxResponse<String>> = callbackFlow {
        val ref = authNetworkDataSource.removeProfileDatabase()
        val updates = hashMapOf<String, Any>("photo" to "")

        ref.update(updates).addOnSuccessListener {
                trySend(CinemaxResponse.Success("Image URL Removed Successfully from Firestore"))
                close()
            }
            .addOnFailureListener {
                trySend(CinemaxResponse.Failure("Failed to Remove Image URL"))
                close()
            }

        awaitClose() // Close the flow when the scope is cancelled
    }

    override fun deleteProfileFromStorage(imageUrl: String) : Flow<CinemaxResponse<String>> = callbackFlow{
        send(CinemaxResponse.Loading)
        val storageRef = authNetworkDataSource.deleteStorage(imageUrl)
        storageRef.delete().addOnSuccessListener {
            trySend(CinemaxResponse.Success("Image Deleted Successfully from Storage"))
            launch {
                removeProfileImage().collectLatest { response ->
                    if (response is CinemaxResponse.Success) {
                        authNetworkDataSource.updatePhoto()
                    }
                    trySend(response) // Send response only once
                }
            }
        }.addOnFailureListener { exception ->
            trySend(CinemaxResponse.Failure(exception.localizedMessage ?: "Failed to Delete Image from Storage"))
        }

        awaitClose()
    }
}