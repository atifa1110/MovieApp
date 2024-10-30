package com.example.movieapp.search.presentation

sealed interface SearchEvent {
    data class ChangeQuery(val value: String) : SearchEvent
    object Refresh : SearchEvent
    object Retry : SearchEvent
    object ClearError : SearchEvent
}