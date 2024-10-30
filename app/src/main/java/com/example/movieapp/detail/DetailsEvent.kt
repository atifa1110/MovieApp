package com.example.movieapp.detail

import com.example.movieapp.search.presentation.SearchEvent

sealed interface DetailsEvent {
    object Refresh : DetailsEvent
    object Retry : DetailsEvent
    object ClearUserMessage : DetailsEvent
    object WishlistMovie : DetailsEvent
//    object WishlistTv : DetailsEvent
}