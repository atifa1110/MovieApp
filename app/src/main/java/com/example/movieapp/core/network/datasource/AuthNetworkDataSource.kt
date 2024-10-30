package com.example.movieapp.core.network.datasource

import android.net.Uri
import com.example.movieapp.login.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class AuthNetworkDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage : FirebaseStorage
) {
    fun getUserUid(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun signOut(){
        firebaseAuth.signOut()
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email,password)
    }

    fun saveUser(
        userUid: String,
        user : User
    ) : Task<Void> {
        return firestore.collection("Users").document(userUid).collection("info").document("profile").set(user)
    }

    fun getProfile (
        userUid: String
    ) : Task<QuerySnapshot> {
        return firestore.collection("Users").document(userUid).collection("info").get()
    }

    fun saveProfile (
        userUid: String,
        user: User
    ) : Task<Void> {
        val updates = hashMapOf<String, Any?>(
            "name" to user.name,
            "email" to user.email,
            "phoneNumber" to user.phoneNumber,
            "photo" to user.photo
        )
        return firestore.collection("Users").document(userUid).collection("info").document("profile").update(updates)
    }

    fun uploadStorage() : StorageReference {
        return storage.reference.child("profile").child("images/${firebaseAuth.currentUser?.uid}.jpeg")
    }

    fun deleteStorage(imageUrl : String): StorageReference {
        return storage.getReferenceFromUrl(imageUrl)
    }

    fun removeProfileDatabase(): DocumentReference {
        return firestore.collection("Users").document(firebaseAuth.currentUser?.uid?:"").collection("info").document("profile")
    }
}