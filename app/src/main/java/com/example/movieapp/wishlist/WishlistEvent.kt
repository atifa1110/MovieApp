package com.example.movieapp.wishlist

sealed interface WishlistEvent {
    object Refresh : WishlistEvent
    object Retry : WishlistEvent
    object ClearError : WishlistEvent
}