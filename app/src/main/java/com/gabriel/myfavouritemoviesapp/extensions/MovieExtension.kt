package com.gabriel.myfavouritemoviesapp.extensions

import com.gabriel.domain.model.Movie
import java.math.RoundingMode
import java.text.DecimalFormat

private const val VOTE_RATING_PATTERN = "#.#"

fun Movie.getOriginalTitle(): String = if (title == originalTitle)
    ""
else
    originalTitle

fun Movie.getPopularity(): String = "Popularity: $popularity"

fun Movie.getVoteAverage(): String = DecimalFormat(VOTE_RATING_PATTERN)
    .apply {
        roundingMode = RoundingMode.CEILING
    }.format(voteAverage / 2).toString()

fun Movie.getReleaseDateFormatted() = "Release date: $releaseDate"