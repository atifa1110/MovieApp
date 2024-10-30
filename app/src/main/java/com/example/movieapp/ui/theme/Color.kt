package com.example.movieapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val BlueFilter = Color(0xFF334BFB)

val Dark = Color(0xFF1F1D2B)
val Soft = Color(0xFF252836)
val BlueAccent = Color(0xFF12CDD9)

val Green = Color(0xFF22B07D)
val Orange = Color(0xFFFF8700)
val Red = Color(0xFFFF7256)
val Black = Color(0xFF171725)
val DarkGrey = Color(0xFF696974)
val Grey = Color(0xFF92929D)
val WhiteGrey = Color(0xFFF1F1F5)
val White = Color(0xFFFFFFFF)
val LineDark = Color(0xFFEAEAEA)

val Star = Color(0x52252836)

@Immutable
data class MovieColors(
    val default: Color = Color.Unspecified,
    val primaryDark: Color = Dark,
    val primarySoft: Color = Soft,
    val primaryBlue: Color = BlueAccent,
    val secondaryGreen: Color = Green,
    val secondaryOrange: Color = Orange,
    val secondaryRed: Color = Red,
    val white: Color = White,
    val whiteGrey: Color = WhiteGrey,
    val black: Color = Black,
    val grey: Color = Grey,
    val darkGrey: Color = DarkGrey,
    val lineDark: Color = LineDark
)

internal val LocalMovieColors = staticCompositionLocalOf { MovieColors() }