package com.example.movieapp.detail.presentation

sealed interface DetailsEvent {
    object Refresh : DetailsEvent
    object Retry : DetailsEvent
    object ClearUserMessage : DetailsEvent
    object WishlistMovie : DetailsEvent
    object WishlistTv : DetailsEvent
}