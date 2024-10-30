package com.example.movieapp.home

sealed interface HomeEvent {
    object Refresh : HomeEvent
    object Retry : HomeEvent
    object ClearError : HomeEvent
}