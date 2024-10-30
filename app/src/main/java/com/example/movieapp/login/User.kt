package com.example.movieapp.login

data class User(
    val name: String? = "",
    val email: String? = "",
    var userId : String? = "",
    var phoneNumber : String? ="",
    var photo : String? = "",
)