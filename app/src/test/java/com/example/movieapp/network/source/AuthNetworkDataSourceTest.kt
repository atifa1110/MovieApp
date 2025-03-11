package com.example.movieapp.network.source

import com.example.movieapp.core.network.datasource.AuthNetworkDataSource
import com.example.movieapp.login.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthNetworkDataSourceTest {

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var firestore: FirebaseFirestore

    @Mock
    private lateinit var storage: FirebaseStorage

    @Mock
    private lateinit var firebaseUser: FirebaseUser

    @Mock
    private lateinit var authResultTask: Task<AuthResult>

    @Mock
    private lateinit var voidTask: Task<Void>

    @Mock
    private lateinit var querySnapshotTask: Task<QuerySnapshot>

    @Mock
    private lateinit var documentReference: DocumentReference

    @Mock
    private lateinit var storageReference: StorageReference

    private lateinit var authNetworkDataSource: AuthNetworkDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        authNetworkDataSource = AuthNetworkDataSource(firebaseAuth, firestore, storage)
    }

    @Test
    fun `getUserUid should return current FirebaseUser`() {
        // Arrange
        `when`(firebaseAuth.currentUser).thenReturn(firebaseUser)

        // Act
        val result = authNetworkDataSource.getUserUid()

        // Assert
        assertEquals(firebaseUser, result)
    }

    @Test
    fun `signOut should call FirebaseAuth signOut`() {
        // Act
        authNetworkDataSource.signOut()

        // Assert
        verify(firebaseAuth).signOut()
    }

    @Test
    fun `signInWithEmailAndPassword should call FirebaseAuth signIn method`() {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        `when`(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(authResultTask)

        // Act
        val result = authNetworkDataSource.signInWithEmailAndPassword(email, password)

        // Assert
        verify(firebaseAuth).signInWithEmailAndPassword(email, password)
        assertEquals(authResultTask, result)
    }

    @Test
    fun `registerWithEmailAndPassword should call FirebaseAuth register method`() {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        `when`(firebaseAuth.createUserWithEmailAndPassword(email, password)).thenReturn(authResultTask)

        // Act
        val result = authNetworkDataSource.registerWithEmailAndPassword(email, password)

        // Assert
        verify(firebaseAuth).createUserWithEmailAndPassword(email, password)
        assertEquals(authResultTask, result)
    }

    @Test
    fun `saveUser should store user data in Firestore`() {
        // Arrange
        val userUid = "12345"
        val user = User("John Doe", "john@example.com", "1234567890", "photoUrl")

        // Mock Firestore references
        val usersCollection = mock<CollectionReference>()
        val userDocument = mock<DocumentReference>()
        val infoCollection = mock<CollectionReference>()
        val profileDocument = mock<DocumentReference>()

        // Mock Firestore behavior
        `when`(firestore.collection("Users")).thenReturn(usersCollection)
        `when`(usersCollection.document(userUid)).thenReturn(userDocument)
        `when`(userDocument.collection("info")).thenReturn(infoCollection)
        `when`(infoCollection.document("profile")).thenReturn(profileDocument)
        `when`(profileDocument.set(user)).thenReturn(voidTask)

        // Act
        val result = authNetworkDataSource.saveUser(userUid, user)

        // Assert
        verify(profileDocument).set(user)
        assertEquals(voidTask, result)
    }

    @Test
    fun `getProfile should retrieve user profile from Firestore`() {
        // Arrange
        val userUid = "12345"
        val usersCollection = mock<CollectionReference>()
        val userDocument = mock<DocumentReference>()
        val infoCollection = mock<CollectionReference>()

        `when`(firestore.collection("Users")).thenReturn(usersCollection)
        `when`(usersCollection.document(userUid)).thenReturn(userDocument)
        `when`(userDocument.collection("info")).thenReturn(infoCollection)
        `when`(infoCollection.get()).thenReturn(querySnapshotTask)

        // Act
        val result = authNetworkDataSource.getProfile(userUid)

        // Assert
        verify(infoCollection).get()
        assertEquals(querySnapshotTask, result)
    }

    @Test
    fun `saveProfile should update user profile in Firestore`() {
        // Arrange
        val userUid = "12345"
        val user = User("John Doe", "john@example.com", "1234567890", "photoUrl")
        val updates = mapOf(
            "name" to user.name,
            "email" to user.email,
            "phoneNumber" to user.phoneNumber,
            "photo" to user.photo
        )

        val usersCollection = mock<CollectionReference>()
        val userDocument = mock<DocumentReference>()
        val infoCollection = mock<CollectionReference>()
        val profileDocument = mock<DocumentReference>()

        `when`(firestore.collection("Users")).thenReturn(usersCollection)
        `when`(usersCollection.document(userUid)).thenReturn(userDocument)
        `when`(userDocument.collection("info")).thenReturn(infoCollection)
        `when`(infoCollection.document("profile")).thenReturn(profileDocument)
        `when`(profileDocument.update(updates)).thenReturn(voidTask)

        // Act
        val result = authNetworkDataSource.saveProfile(userUid, user)

        // Assert
        verify(profileDocument).update(updates)
        assertEquals(voidTask, result)
    }

    @Test
    fun `uploadStorage should return StorageReference for user profile image`() {
        // Arrange
        `when`(firebaseAuth.currentUser).thenReturn(firebaseUser)
        `when`(firebaseUser.uid).thenReturn("12345")
        `when`(storage.reference).thenReturn(storageReference)
        `when`(storageReference.child("profile")).thenReturn(storageReference)
        `when`(storageReference.child("images/12345.jpeg")).thenReturn(storageReference)

        // Act
        val result = authNetworkDataSource.uploadStorage()

        // Assert
        verify(storageReference).child("profile")
        verify(storageReference).child("images/12345.jpeg")
        assertEquals(storageReference, result)
    }

    @Test
    fun `deleteStorage should return StorageReference for given image URL`() {
        // Arrange
        val imageUrl = "https://firebase.storage.com/example.jpg"
        `when`(storage.getReferenceFromUrl(imageUrl)).thenReturn(storageReference)

        // Act
        val result = authNetworkDataSource.deleteStorage(imageUrl)

        // Assert
        verify(storage).getReferenceFromUrl(imageUrl)
        assertEquals(storageReference, result)
    }

    @Test
    fun `removeProfileDatabase should return Firestore DocumentReference`() {
        // Arrange
        `when`(firebaseAuth.currentUser).thenReturn(firebaseUser)
        `when`(firebaseUser.uid).thenReturn("12345")
        `when`(firestore.collection("Users")).thenReturn(mock())
        `when`(firestore.collection("Users").document("12345")).thenReturn(mock())
        `when`(firestore.collection("Users").document("12345").collection("info")).thenReturn(mock())
        `when`(firestore.collection("Users").document("12345").collection("info").document("profile"))
            .thenReturn(documentReference)

        // Act
        val result = authNetworkDataSource.removeProfileDatabase()

        // Assert
        verify(firestore.collection("Users").document("12345").collection("info")).document("profile")
        assertEquals(documentReference, result)
    }
}
