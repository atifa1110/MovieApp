package com.example.movieapp.core.domain

data class WishlistModel(
    val id: Int,
    val mediaType: MediaTypeModel.Wishlist,
    val title : String,
    val genre : List<GenreModel>?,
    val rating : Double,
    val posterPath : String,
    val isWishListed : Boolean
)