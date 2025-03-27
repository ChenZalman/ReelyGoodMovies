    package com.example.reelygoodmovies.utils
    
    import com.example.reelygoodmovies.R
    import com.example.reelygoodmovies.data.models.GenreConverter
    import com.example.reelygoodmovies.data.models.Movie
    import com.google.gson.JsonDeserializationContext
    import com.google.gson.JsonDeserializer
    import com.google.gson.JsonElement
    import com.google.gson.JsonObject
    import java.lang.reflect.Type

    class MovieDeserializer : JsonDeserializer<Movie> {

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Movie {
            val jsonObject = json.asJsonObject

            val id = jsonObject.get("id").asInt
            val title = jsonObject.get("title").asString
            val overview = jsonObject.get("overview").asString

            val trailerUrl = getTrailerUrl(jsonObject)
            val posterUrl = getPosterUrl(jsonObject)
            val voteAverage = getVoteAverage(jsonObject)
            val movieLength = getMovieLength(jsonObject)
            val releaseYear = getReleaseYear(jsonObject)
            val genreNames = getGenreNames(jsonObject)

            return Movie(
                title = title,
                plot = overview,
                rate = voteAverage,
                genre = genreNames,
                photo = posterUrl,
                year = releaseYear,
                length = movieLength,
                id = id,
                localGen = false,
                trailerUrl = trailerUrl
            )
        }

        private fun getTrailerUrl(jsonObject: JsonObject): String {
            val trailer = jsonObject.getAsJsonArray("results")?.find {
                it.asJsonObject.get("type").asString == "Trailer"
            }
            return trailer?.asJsonObject?.get("key")?.asString ?: ""
        }

        private fun getPosterUrl(jsonObject: JsonObject): String {
            val posterBaseUrl = "https://image.tmdb.org/t/p/w500"
            val posterPath = jsonObject.get("poster_path")?.asString
            return posterPath?.let { "$posterBaseUrl$it" } ?: ""
        }

        private fun getVoteAverage(jsonObject: JsonObject): Float {
            val voteAverage = jsonObject.get("vote_average").asFloat
            return voteAverage / 2 // adjusting for the rating scale
        }

        private fun getMovieLength(jsonObject: JsonObject): Int {
            return if (jsonObject.has("runtime")) {
                jsonObject.get("runtime").asInt
            } else {
                0
            }
        }

        private fun getReleaseYear(jsonObject: JsonObject): Int {
            val releaseDate = jsonObject.get("release_date")?.asString
            return if (!releaseDate.isNullOrEmpty() && releaseDate.length >= 4) {
                releaseDate.substring(0, 4).toIntOrNull() ?: 0
            } else {
                0
            }
        }

        private fun getGenreNames(jsonObject: JsonObject): List<Int> {
            val genreIds: List<Int> = when {
                jsonObject.has("genres") -> {
                    jsonObject.getAsJsonArray("genres").mapNotNull {
                        it.asJsonObject.get("id")?.asInt
                    }
                }
                jsonObject.has("genre_ids") -> {
                    jsonObject.getAsJsonArray("genre_ids").mapNotNull {
                        it?.asInt
                    }
                }
                else -> emptyList()
            }

            return genreIds.mapNotNull { GenreConverter.movieGenres[it] ?: R.string.unknown_genre }
        }
    }
