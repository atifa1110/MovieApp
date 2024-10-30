package com.example.movieapp.core.network

import android.content.Context
import com.example.movieapp.R
import com.example.movieapp.core.model.Genre
import com.example.movieapp.core.ui.asNames
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun getDateCustomFormat(releaseDate: String?, firstAirDate: String?): String {
    // Determine which date string to use
    val dateString = releaseDate?.takeIf { it.isNotEmpty() } ?: firstAirDate?.takeIf { it.isNotEmpty() }

    // Check if the dateString is null or empty
    if (dateString.isNullOrEmpty()) {
        return "No Release Date"
    }

    return try {
        // Parse the original date string
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date? = inputFormat.parse(dateString)

        // Define the desired output format
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        // Format the date to the desired string
        if (date != null) {
            "On " + outputFormat.format(date)
        } else {
            "No Release Date"
        }
    } catch (e: Exception) {
        "No Release Date"
    }
}

fun getYearFromFormattedDate(dateString: String): String {
    return try {
        // Define the input format that matches the given date string
        val inputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        // Remove the "On " prefix and parse the date
        val date = inputFormat.parse(dateString.removePrefix("On "))

        // Extract the year using SimpleDateFormat
        val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        if (date != null) {
            outputFormat.format(date)
        } else {
            "No Release Date"
        }
    } catch (e: Exception) {
        "No Release Date"
    }
}

fun calculateStarRating(voteAverage: Double?): Double {
    // Map the 1-10 vote average to a 1-5 star rating
    val starRating = voteAverage?.div(2)?.coerceIn(1.0, 5.0)?:0.0
    return String.format("%.1f", starRating).toDouble()
}

fun getTitle(originalTitle : String?, originalName: String?):String{
    return if(originalTitle.isNullOrEmpty()) originalName.toString() else originalTitle.toString()
}

fun getPhoto(posterUrl : String?, profilePath: String?):String{
    return if(posterUrl.isNullOrEmpty()) profilePath.toString() else posterUrl.toString()
}

fun capitalizeFirstLetter(input: String?): String {
    return if (input?.isNotEmpty() == true) {
        input.substring(0, 1).uppercase() + input.substring(1).lowercase()
    } else {
        input?:""
    }
}

fun getGenreName(genre: List<Genre>?): Int {
    return try {
        genre?.get(0)?.nameResourceId?: R.string.none
    } catch (e: IndexOutOfBoundsException) {
        R.string.none
    }
}

fun String.asMediaType() = capitalizeFirstLetter(this)
fun String.asImageURL() = buildImageUrl(path = this)
fun buildImageUrl(path: String) = Constants.IMAGE_URL + path

fun Double.getRating() = calculateStarRating(voteAverage = this)
fun String.getFormatReleaseDate() = getDateCustomFormat(releaseDate = this, firstAirDate = null)
fun String.getYearReleaseDate() = getYearFromFormattedDate(dateString = this)

fun String.asVideoURL(site: String) = buildVideoUrl(key = this, site = site)
fun buildVideoUrl(key: String, site :String) : String {
    val url = if(site == "YouTube"){
        Constants.VIDEO_URL + key
    }else {
        "None"
    }
    return url
}
