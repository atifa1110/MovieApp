package com.example.movieapp.core.ui

interface EventHandler<E> {
    fun onEvent(event: E)
}