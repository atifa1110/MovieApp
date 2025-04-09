package com.example.movieapp

import android.content.ContentUris
import android.Manifest
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movieapp.profile.presentation.EditProfileScreen
import com.example.movieapp.profile.presentation.EditProfileUiState
import com.example.movieapp.ui.theme.MovieAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditProfileScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Test Cases :
    // verify display all data correctly
    // click profile image select image from gallery
    // click profile image select image from camera
    // click delete profile image
    // click save changes

    @Before
    fun setup() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.uiAutomation.grantRuntimePermission(
            instrumentation.targetContext.packageName,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    }

    @Test
    fun editProfileScreenDisplaysCorrectUI() {
        composeTestRule.setContent {
            MovieAppTheme {
                EditProfileScreen(
                    uiState = EditProfileUiState(
                        userName = "Atifa Fiorenza",
                        email = "atifafiorenza24@gmail.com",
                        phoneNumber = "87878601919",
                        photo = "",

                    ),
                    imageUri = null,
                    onNameChange = {},
                    onPhoneChange = {},
                    onSheetOpen = {},
                    onImageSelected = {},
                    onDeleteProfile = {},
                    uploadProfile = {},
                    snackBarMessageShown = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Atifa Fiorenza").assertIsDisplayed()
        composeTestRule.onNodeWithText("atifafiorenza24@gmail.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("87878601919").assertIsDisplayed()
    }

    @Test
    fun editProfileScreenUpdatesNamePhoneEmptyProfileCorrectly() {

        composeTestRule.setContent {
            MovieAppTheme {
                val uiState = remember{ mutableStateOf(
                    EditProfileUiState(
                        userName = "Atifa Fiorenza", email = "atifafiorenza24@gmail.com",
                        phoneNumber = "87878601919",photo = "")
                )}
                EditProfileScreen(
                    uiState = uiState.value,
                    imageUri = null,
                    onNameChange = {
                        uiState.value = uiState.value.copy(
                            userName = it
                        )
                    },
                    onPhoneChange = {
                        uiState.value = uiState.value.copy(
                           phoneNumber = it
                        )
                    },
                    onSheetOpen = {},
                    onImageSelected = {},
                    onDeleteProfile = {},
                    uploadProfile = {},
                    snackBarMessageShown = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("TextInput").assertIsDisplayed().performTextInput("atifa")
        composeTestRule.onNodeWithTag("PhoneInput").assertIsDisplayed().performTextInput("8080808080")
    }

    @Test
    fun editProfileScreenUpdatesNamePhoneGalleryProfileCorrectly() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val contentResolver = instrumentation.targetContext.contentResolver
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(MediaStore.Images.Media._ID)

        val cursor = contentResolver.query(collection, projection, null, null, null)

        var imageUri: Uri? = null

        cursor?.use {
            if (it.moveToFirst()) {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val id = it.getLong(idColumn)
                imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.d("EditProfileTest", "Image URI found: $imageUri")
            } else {
                Log.d("EditProfileTest", "No images found in MediaStore.")
            }
        }

        if (imageUri == null) {
            throw IllegalStateException("No image found in gallery for testing.")
        }

        val uiState = mutableStateOf(
            EditProfileUiState(
                userName = "Atifa Fiorenza",
                email = "atifafiorenza24@gmail.com",
                phoneNumber = "87878601919",
                photo = "",
                isSheetOpen = false
            )
        )

        composeTestRule.setContent {
            MovieAppTheme {
                EditProfileScreen(
                    uiState = uiState.value,
                    imageUri = imageUri,
                    onNameChange = {
                        uiState.value = uiState.value.copy(userName = it)
                    },
                    onPhoneChange = {
                        uiState.value = uiState.value.copy(phoneNumber = it)
                    },
                    onSheetOpen = {
                        uiState.value = uiState.value.copy(isSheetOpen = it)
                    },
                    onImageSelected = { selectedUri ->
                        uiState.value = uiState.value.copy(
                            photo = selectedUri?.toString() ?: "", isSheetOpen = false)
                    },
                    onDeleteProfile = {},
                    uploadProfile = {},
                    snackBarMessageShown = {}
                )
            }
        }

        // Step 1: Click profile image to open bottom sheet
        composeTestRule.onNodeWithTag("SelectImage").performClick()

        // Step 2: Assert bottom sheet is displayed
        composeTestRule.onNodeWithTag("ModalBottomSheet").assertExists()

        // Step 3: Verify buttons inside the bottom sheet
        composeTestRule.onNodeWithTag("SelectGallery").assertExists()
        composeTestRule.onNodeWithTag("TakePhoto").assertExists()
        composeTestRule.onNodeWithTag("DeletePhoto").assertExists()

        // Step 4: Simulate selecting an image from the gallery
        composeTestRule.onNodeWithTag("SelectGallery").performClick()

        // Step 5: Wait for UI update
        composeTestRule.waitForIdle()

        // Step 10: Verify that the bottom sheet is closed
        composeTestRule.onNodeWithTag("ModalBottomSheet").assertDoesNotExist()

    }


    @Test
    fun editProfileScreenUpdatesNamePhoneCameraProfileCorrectly() {

        val uiState = mutableStateOf(
            EditProfileUiState(
                userName = "Atifa Fiorenza",
                email = "atifafiorenza24@gmail.com",
                phoneNumber = "87878601919",
                photo = "",
                isSheetOpen = false
            )
        )
        val fakeUri = Uri.parse("file:///storage/emulated/0/Pictures/test_image.jpg")

        composeTestRule.setContent {
            MovieAppTheme {
                EditProfileScreen(
                    uiState = uiState.value,
                    imageUri = fakeUri,
                    onNameChange = {
                        uiState.value = uiState.value.copy(
                            userName = it
                        )
                    },
                    onPhoneChange = {
                        uiState.value = uiState.value.copy(
                            phoneNumber = it
                        )
                    },
                    onSheetOpen = {
                        uiState.value = uiState.value.copy(
                            isSheetOpen = it
                        )
                    },
                    onImageSelected = {
                        uiState.value = uiState.value.copy(
                            photo = it?.toString() ?: ""
                        )
                    },
                    onDeleteProfile = {},
                    uploadProfile = {},
                    snackBarMessageShown = {}
                )
            }
        }

        // Step 1: Click profile image to open bottom sheet
        composeTestRule.onNodeWithTag("SelectImage").performClick()

        // Step 3: Assert bottom sheet is displayed
        composeTestRule.onNodeWithTag("ModalBottomSheet").assertExists()

        // Step 4: Verify buttons inside the bottom sheet
        composeTestRule.onNodeWithTag("SelectGallery").assertExists()
        composeTestRule.onNodeWithTag("TakePhoto").assertExists()
        composeTestRule.onNodeWithTag("DeletePhoto").assertExists()

        composeTestRule.onNodeWithTag("SelectGallery").performClick()

        // Step 7: Wait for UI update
        composeTestRule.waitForIdle()

        // Step 5: Wait for AsyncImage to update
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("SelectImage").fetchSemanticsNodes().isNotEmpty()
        }

    }

    @Test
    fun editProfileScreenUpdatesNamePhoneDeleteProfileCorrectly() {

        var fakeUri = Uri.parse("file:///storage/emulated/0/Pictures/test_image.jpg")

        val uiState = mutableStateOf(
            EditProfileUiState(
                userName = "Atifa Fiorenza",
                email = "atifafiorenza24@gmail.com",
                phoneNumber = "87878601919",
                photo = "",
                isSheetOpen = false
            )
        )

        composeTestRule.setContent {
            MovieAppTheme {
                EditProfileScreen(
                    uiState = uiState.value,
                    imageUri = fakeUri,
                    onNameChange = {
                        uiState.value = uiState.value.copy(
                            userName = it
                        )
                    },
                    onPhoneChange = {
                        uiState.value = uiState.value.copy(
                            phoneNumber = it
                        )
                    },
                    onSheetOpen = {
                        uiState.value = uiState.value.copy(
                            isSheetOpen = it
                        )
                    },
                    onImageSelected = {},
                    onDeleteProfile = {
                        fakeUri = null
                        uiState.value = uiState.value.copy(
                            photo = null
                        )
                    },
                    uploadProfile = {},
                    snackBarMessageShown = {}
                )
            }
        }

        // Step 1: Click profile image to open bottom sheet
        composeTestRule.onNodeWithTag("SelectImage").performClick()

        // Step 3: Assert bottom sheet is displayed
        composeTestRule.onNodeWithTag("ModalBottomSheet").assertExists()

        // Step 4: Verify buttons inside the bottom sheet
        composeTestRule.onNodeWithTag("SelectGallery").assertExists()
        composeTestRule.onNodeWithTag("TakePhoto").assertExists()
        composeTestRule.onNodeWithTag("DeletePhoto").assertExists()

        composeTestRule.onNodeWithTag("DeletePhoto").performClick()
    }

    @Test
    fun editProfileScreenSaveUploadProfile() {
        var uploadCalled = false
        composeTestRule.setContent {
            MovieAppTheme {
                EditProfileScreen(
                    uiState = EditProfileUiState(userName = "", email = "", phoneNumber = ""),
                    imageUri = null,
                    onNameChange = {},
                    onPhoneChange = {},
                    onSheetOpen = {},
                    onImageSelected = {},
                    onDeleteProfile = {},
                    uploadProfile = { uploadCalled = true },
                    snackBarMessageShown = {}
                )
            }
        }

        // "Save Changes" 버튼 클릭
        composeTestRule.onNodeWithText("Save Changes").performClick()
        assert(uploadCalled)
    }

}
