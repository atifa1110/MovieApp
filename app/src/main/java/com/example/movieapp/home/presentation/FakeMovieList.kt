package com.example.movieapp.home.presentation

import com.example.movieapp.R
import com.example.movieapp.core.domain.MediaTypeModel
import com.example.movieapp.core.model.Cast
import com.example.movieapp.core.model.Credits
import com.example.movieapp.core.model.Crew
import com.example.movieapp.core.model.Genre
import com.example.movieapp.core.model.Images
import com.example.movieapp.core.model.Movie
import com.example.movieapp.core.model.MovieDetails
import com.example.movieapp.core.model.Season
import com.example.movieapp.core.model.TvShow
import com.example.movieapp.core.model.TvShowDetails
import com.example.movieapp.core.model.Video
import com.example.movieapp.core.model.Videos
import com.example.movieapp.core.model.WishList

fun getFakeMovieList() = List(10) { index ->
    Movie(
        index,
        "Avatar Ages: Dreams",
        false,
        "On the night of dreams Avatar performed Hunter Gatherer in its entirety, plus a selection of their most popular songs.  Originally aired January 9th 2021",
        "Movie",
        "March 2, 2022",
        emptyList(),
        4.0,
        "/dj8g4jrYMfK6tQ26ra3IaqOx5Ho.jpg",
        "/4twG59wnuHpGIRR9gYsqZnVysSP.jpg",
        "",
        92
    )
}

fun getFakeMovieActor() : Movie {
    return  Movie(
        1,
        "John Watt",false,
        "","","",
        emptyList(),0.0,"",
        "","",92)
}

fun getFakeDetailMovie() : MovieDetails {
    return MovieDetails(1,"Avatar Ages: Dreams","Learn about the history, usage and variations of Lorem Ipsum, the industry's standard dummy text for printing and typesetting. Generate your own Lorem Ipsum with a dictionary of over 200 Latin words and model sentence structures.","",3000000,
        listOf(Genre(R.string.action),Genre(R.string.adventure)),"","2010-07-16",32,
        false,0.0,3,
        Credits(listOf(
            Cast(1,"Adam","Character",""),
            Cast(2,"Adam","Character","")
        ), listOf(
            Crew(1,"Jennifer","PD",""),
            Crew(2,"Jennifer","PD","")
        )),
        4.2,
        Images(emptyList(), emptyList()),
        Videos(listOf(Video(id = "1",language = "",country="",key="1",name="Video 1",site="YouTube",size=1,type="Teaser",official=true,publishedAt=""))),false
    )
}

fun getFakeDetailTvShow() : TvShowDetails {
    return TvShowDetails(
        id = 1,name = "Avatar Ages: Dreams",adult=false,backdropPath = "", episodeRunTime = emptyList(), firstAirDate = "",
        genres = listOf(Genre(R.string.action),Genre(R.string.adventure)), seasons = listOf(Season(airDate = "", episodeCount = 1,id = 0,name ="Season 1", overview="", posterPath = "", seasonNumber = 1,rating = "3")) ,homepage = "", inProduction = false,languages = emptyList(),lastAirDate = "", numberOfEpisodes = 3, numberOfSeasons = 1, originCountry = emptyList(), originalLanguage = "eng", originalName = "",
        overview ="Learn about the history, usage and variations of Lorem Ipsum, the industry's standard dummy text for printing and typesetting. Generate your own Lorem Ipsum with a dictionary of over 200 Latin words and model sentence structures.",
        popularity = 0.0, posterPath = "",status = "",tagline = "", type ="", voteAverage = 0.0,voteCount = 0,
        credits = Credits(listOf(
            Cast(1,"Adam","Character",""),
            Cast(2,"Adam","Character","")
        ), listOf(
            Crew(1,"Jennifer","PD",""),
            Crew(2,"Jennifer","PD","")
        )),
        rating = 4.2,
        isWishListed = false,
    )
}

fun getFakeWishListed() : WishList{
    return WishList(1,MediaTypeModel.Wishlist.Movie,
        "Avatar Ages: Dreams", listOf(Genre(R.string.adventure),Genre(R.string.action)),4.2,
        "",true
    )
}

fun getFakeWishListed(id: Int,name: String) : WishList{
    return WishList(id,MediaTypeModel.Wishlist.Movie,
        name, listOf(Genre(R.string.adventure),Genre(R.string.action)),4.2,
        "",true
    )
}

fun getFakeMovie() : Movie {
    return Movie(
        1,
        "Avatar Ages: Dreams",
        false,
        "On the night of dreams Avatar performed Hunter Gatherer in its entirety, plus a selection of their most popular songs.  Originally aired January 9th 2021",
        "Movie",
        "2021",
        listOf(Genre(R.string.action), Genre(R.string.adventure)),
        4.0,
        "/dj8g4jrYMfK6tQ26ra3IaqOx5Ho.jpg",
        "/4twG59wnuHpGIRR9gYsqZnVysSP.jpg",
        "",
        92
    )
}

fun getFakeMovie(id: Int,name: String) : Movie {
    return Movie(
        id,
        name,
        false,
        "On the night of dreams Avatar performed Hunter Gatherer in its entirety, plus a selection of their most popular songs.  Originally aired January 9th 2021",
        "Movie",
        "2021",
        listOf(Genre(R.string.action), Genre(R.string.adventure)),
        4.0,
        "/dj8g4jrYMfK6tQ26ra3IaqOx5Ho.jpg",
        "/4twG59wnuHpGIRR9gYsqZnVysSP.jpg",
        "",
        92
    )
}


fun getFakeTvShow() : TvShow {
    return TvShow(
        1,
        "Avatar Ages: Dreams",
        "On the night of dreams Avatar performed Hunter Gatherer in its entirety, plus a selection of their most popular songs.  Originally aired January 9th 2021",
        "2021",
        listOf(Genre(R.string.action), Genre(R.string.adventure)),
        0.0,
        "",
        "",
        9.2
    )
}

fun getFakeTvShow(id: Int,name: String) : TvShow {
    return TvShow(
        id,
        name,
        "On the night of dreams Avatar performed Hunter Gatherer in its entirety, plus a selection of their most popular songs.  Originally aired January 9th 2021",
        "2021",
        listOf(Genre(R.string.action), Genre(R.string.adventure)),
        0.0,
        "",
        "",
        9.2
    )
}

fun getFakeMovieListModel() = List(10) { index ->
    Movie(index,"Movie",false,
        "","Movie",
        "2021",
        listOf(Genre(R.string.action),Genre(R.string.adventure)),
        0.0,"Original Movie",
        "","",92
    )
}

fun getFakeMovieList2Model() = List(2) { index ->
    Movie(index,"Movie",false,
        "","Movie", "2021",
        listOf(Genre(R.string.action),Genre(R.string.adventure)),
        0.0,"Original Movie",
        "","",92
    )
}