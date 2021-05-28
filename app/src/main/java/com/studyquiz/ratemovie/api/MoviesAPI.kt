package com.studyquiz.ratemovie.api

import com.studyquiz.ratemovie.models.MovieListResponse
import com.studyquiz.ratemovie.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {

    @GET("3/discover/movie")
    suspend fun getMoviesList(
        @Query("country")
        countryCode: String = "en-US",
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MovieListResponse>
//
//    @GET("3/movie/{movie_id}")
//    suspend fun getMovieById(
//        @Path("movie_id")
//        movieId: Int,
//        @Query("apiKey")
//        apiKey: String = API_KEY
//    ): Response<MovieByIdResponse>
}