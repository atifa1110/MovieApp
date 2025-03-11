package com.example.movieapp.core.database.util

sealed interface MediaType {
    enum class Movie(val mediaType: String) : MediaType {
        Upcoming(MovieUpcomingMediaType),
        TopRated(MovieTopRatedMediaType),
        Popular(MoviePopularMediaType),
        NowPlaying(MovieNowPlayingMediaType),
        Discover(MovieDiscoverMediaType),
        Trending(MovieTrendingMediaType);

        companion object {
            private val mediaTypes = values().associateBy(Movie::mediaType)
            operator fun get(mediaType: String) = checkNotNull(mediaTypes[mediaType]) {
                "$InvalidMediaTypeMessage $mediaType"
            }
        }
    }

    enum class TvShow(val mediaType: String) : MediaType {
        TopRated(TopRatedMediaType),
        Popular(PopularMediaType),
        NowPlaying(NowPlayingMediaType),
        Discover(DiscoverMediaType),
        Trending(TrendingMediaType);

        companion object {
            private val mediaTypes = values().associateBy(TvShow::mediaType)
            operator fun get(mediaType: String) = checkNotNull(mediaTypes[mediaType]) {
                "$InvalidMediaTypeMessage $mediaType"
            }
        }
    }

    enum class Common(val mediaType: String) : MediaType {
        Upcoming(UpcomingMediaType),
        TopRated(TopRatedMediaType),
        Popular(PopularMediaType),
        NowPlaying(NowPlayingMediaType),
        Discover(DiscoverMediaType),
        Trending(TrendingMediaType);

        companion object {
            private val mediaTypes = values().associateBy(Common::mediaType)
            operator fun get(mediaType: String) = checkNotNull(mediaTypes[mediaType]) {
                "$InvalidMediaTypeMessage $mediaType"
            }
        }
    }

    enum class Wishlist { Movie, TvShow }
}

private const val UpcomingMediaType = "upcoming"
private const val TopRatedMediaType = "top_rated"
private const val PopularMediaType = "popular"
private const val NowPlayingMediaType = "now_playing"
private const val DiscoverMediaType = "discover"
private const val TrendingMediaType = "trending"

private const val MovieUpcomingMediaType = "movie_upcoming"
private const val MovieTopRatedMediaType = "movie_top_rated"
private const val MoviePopularMediaType = "movie_popular"
private const val MovieNowPlayingMediaType = "movie_now_playing"
private const val MovieDiscoverMediaType = "movie_discover"
private const val MovieTrendingMediaType = "movie_trending"

private const val InvalidMediaTypeMessage = "Invalid media type."