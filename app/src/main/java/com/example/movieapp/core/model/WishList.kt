package com.example.movieapp.core.model

import com.example.movieapp.core.domain.GenreModel
import com.example.movieapp.core.domain.MediaTypeModel

data class WishList(
    val id: Int,
    val mediaType: MediaTypeModel.Wishlist,
    val title : String,
    val genres : List<Genre>?,
    val rating : Double,
    val posterPath : String,
    val isWishListed : Boolean
)