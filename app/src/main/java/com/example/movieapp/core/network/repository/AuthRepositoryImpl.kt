package com.example.movieapp.core.network.repository

import com.example.movieapp.core.network.datasource.AuthNetworkDataSource
import com.example.movieapp.core.network.response.CinemaxResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.movieapp.login.User
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl @Inject constructor(
    private val authNetworkDataSource: AuthNetworkDataSource
) : AuthRepository {

    override fun getUserId(): FirebaseUser? {
        return authNetworkDataSource.getUserUid()
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<CinemaxResponse<String>> = callbackFlow {
        // Emit loading state
        send(CinemaxResponse.Loading)

        val task = authNetworkDataSource.signInWithEmailAndPassword(email, password)

        // Use a listener for the task completion
        task.addOnCompleteListener { taskResult ->
            if (taskResult.isSuccessful) {
                // Emit success state
                trySend(CinemaxResponse.Success("Login successfully!")).isSuccess
            } else {
                // Emit failure state if not successful
                trySend(CinemaxResponse.Failure("Authentication failed, Check email and password")).isSuccess
            }
            close()
        }.addOnFailureListener { exception ->
            // Emit failure on exception
            trySend(CinemaxResponse.Failure("Authentication failed: ${exception.message}")).isSuccess
            close()
        }

        // Provide a cancellation mechanism
        awaitClose { task.isCanceled }
    }

    override fun registerWithEmailAndPassword(
        email: String,
        password: String,
        user: User
    ): Flow<CinemaxResponse<String>> = callbackFlow {
        // Emit loading state
        send(CinemaxResponse.Loading)

        val task = authNetworkDataSource.registerWithEmailAndPassword(email,password)
        // Use a listener for the task completion
        task.addOnCompleteListener { taskResult ->
            trySend(CinemaxResponse.Loading)
            if(taskResult.isSuccessful){
                // Get the created FirebaseUser
                val firebaseUser = taskResult.result?.user
                if(firebaseUser != null){
                    user.userId = firebaseUser.uid
                    //user.userId = taskResult.result.user?.uid?:""

                    // Build a profile update request
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(user.name)
                        .build()

                    firebaseUser.updateProfile(profileUpdates).addOnCompleteListener{ profileTask ->
                        if(profileTask.isSuccessful){
                            updateUserInfo(user) { state ->
                                when (state) {
                                    is CinemaxResponse.Failure -> trySend(CinemaxResponse.Failure(state.error))
                                    is CinemaxResponse.Loading -> trySend(CinemaxResponse.Loading)
                                    is CinemaxResponse.Success -> trySend(CinemaxResponse.Success(state.value))
                                }
                                close()
                            }
                        }else{
                            trySend(CinemaxResponse.Failure(profileTask.exception?.message ?: "Failed to update profile"))
                            close()
                        }
                    }
                }else{
                    trySend(CinemaxResponse.Failure("Registration failed: Unable to retrieve FirebaseUser."))
                    close()
                }
            }else{
                try {
                    throw taskResult.exception ?: java.lang.Exception("Invalid authentication")
                } catch (e: FirebaseAuthWeakPasswordException) {
                    trySend(CinemaxResponse.Failure("Authentication failed, Password should be at least 8 characters"))
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    trySend(CinemaxResponse.Failure("Authentication failed, Invalid email entered"))
                } catch (e: FirebaseAuthUserCollisionException) {
                    trySend(CinemaxResponse.Failure("Authentication failed, Email already registered."))
                } catch (e: Exception) {
                    trySend(CinemaxResponse.Failure(e.message?:"Unexpected Error"))
                }
                close()
            }
        }.addOnFailureListener{
            trySend(CinemaxResponse.Failure(it.localizedMessage?:"Unexpected Error"))
            close()
        }

        // Provide a cancellation mechanism
        awaitClose { task.isCanceled }
    }

    override fun updateUserInfo(user: User, result: (CinemaxResponse<String>) -> Unit) {
        result.invoke(CinemaxResponse.Loading)
        authNetworkDataSource.saveUser(user.userId?:"",user)
            .addOnSuccessListener {
                result.invoke(
                    CinemaxResponse.Success("User has been update successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    CinemaxResponse.Failure(it.localizedMessage?:"Unexpected Error")
                )
            }
    }

    override fun logout() {
        authNetworkDataSource.signOut()
    }

    override fun getProfile(): Flow<CinemaxResponse<User>> = callbackFlow {
        // Emit loading state
        send(CinemaxResponse.Loading)
        // Get the user UID (assuming it's non-null)
        val userUid = authNetworkDataSource.getUserUid()?.uid ?: ""
        val task = authNetworkDataSource.getProfile(userUid)

        task.addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                // Iterate through the documents in the "info" collection
                for (documentSnapshot in querySnapshot.documents) {
                    // You can access each document's data here
                    val user = documentSnapshot.toObject(User::class.java)?:User()
                    trySend(CinemaxResponse.Success(user))
                }
            }else{
                trySend(CinemaxResponse.Failure("No user data found"))
            }
            close()
        }.addOnFailureListener {
            trySend(CinemaxResponse.Failure("Error fetching user data"))
            close()
        }

        // Provide a cancellation mechanism
        awaitClose { task.isCanceled }
    }

    override fun saveProfile(user: User): Flow<CinemaxResponse<String>> = callbackFlow {
        // Emit loading state
        send(CinemaxResponse.Loading)

        val userUid = authNetworkDataSource.getUserUid()?.uid ?: ""
        val task = authNetworkDataSource.saveProfile(userUid, user).addOnSuccessListener { task->
            trySend(CinemaxResponse.Success("Data is Save"))
            close()
        }.addOnFailureListener {
            trySend(CinemaxResponse.Failure("Data Failed To Save"))
            close()
        }
        // Provide a cancellation mechanism
        awaitClose { task.isCanceled }
    }

}