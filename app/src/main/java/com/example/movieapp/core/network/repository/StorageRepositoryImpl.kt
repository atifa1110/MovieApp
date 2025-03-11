package com.example.movieapp.core.network.repository

import android.net.Uri
import com.example.movieapp.core.network.datasource.AuthNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import com.example.movieapp.login.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val authNetworkDataSource: AuthNetworkDataSource
) : StorageRepository{

    override fun saveProfile(user: User, result: (CinemaxResponse<String>) -> Unit) {
        val userUid = authNetworkDataSource.getUserUid()?.uid ?: ""
        authNetworkDataSource.saveProfile(userUid, user).addOnSuccessListener {
            result(CinemaxResponse.Success("Data is Saved Successfully"))
        }.addOnFailureListener {
            result(CinemaxResponse.Failure("Data is Failed To Save"))
        }
    }

    override fun uploadImageAndSaveUri(user: User,imageUri: Uri): Flow<CinemaxResponse<String>> = callbackFlow {
        send(CinemaxResponse.Loading)
        val fileRef = authNetworkDataSource.uploadStorage()
        val task = fileRef.putFile(imageUri)

        task.addOnCompleteListener{ result->
            if(result.isSuccessful) {
                fileRef.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    user.photo = downloadUrl
                    saveProfile(user){ response->
                        when(response){
                            is CinemaxResponse.Failure -> trySend(CinemaxResponse.Failure(response.error))
                            is CinemaxResponse.Loading -> trySend(CinemaxResponse.Loading)
                            is CinemaxResponse.Success -> trySend(CinemaxResponse.Success(response.value))
                        }
                    }
                }
                trySend(CinemaxResponse.Success("Upload to Storage is Success"))
            }else{
                trySend(CinemaxResponse.Failure("Upload to Storage is Failed"))
            }
        }.addOnFailureListener{
            trySend(CinemaxResponse.Failure("Unexpected Error"))
        }

        // Provide a cancellation mechanism
        awaitClose { task.cancel() }
    }

    fun removeProfileImage(onComplete: (CinemaxResponse<String>) -> Unit) : Task<Void> {
        val ref = authNetworkDataSource.removeProfileDatabase()

        val updates = hashMapOf<String, Any>(
            "photo" to ""
        )
        // i add return cause the function return task<void>
        return ref.update(updates)
            .addOnSuccessListener {
                onComplete(CinemaxResponse.Success("Image URL Removed Successfully from Firestore"))
            }
            .addOnFailureListener { exception ->
                onComplete(CinemaxResponse.Failure(exception.localizedMessage ?: "Failed to Remove Image URL from Firestore"))
            }
    }

    override fun deleteProfileFromStorage(imageUrl: String) : Flow<CinemaxResponse<String>> = callbackFlow{
        send(CinemaxResponse.Loading)
        val storageRef = authNetworkDataSource.deleteStorage(imageUrl)
        storageRef.delete()
            .addOnSuccessListener {
                trySend(CinemaxResponse.Success("Image Deleted Successfully from Storage"))
                removeProfileImage{ response ->
                    when(response){
                        is CinemaxResponse.Failure -> trySend(CinemaxResponse.Failure(response.error))
                        CinemaxResponse.Loading -> trySend(CinemaxResponse.Loading)
                        is CinemaxResponse.Success -> trySend(CinemaxResponse.Success(response.value))
                    }
                }
            }
            .addOnFailureListener { exception ->
                trySend(CinemaxResponse.Failure(exception.localizedMessage ?: "Failed to Delete Image from Storage"))
            }
        awaitClose {  storageRef.delete() }
    }
}