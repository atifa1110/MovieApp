package com.example.movieapp.home.presentation

sealed interface HomeEvent {
    object Refresh : HomeEvent
    object Retry : HomeEvent
    object ClearError : HomeEvent
}