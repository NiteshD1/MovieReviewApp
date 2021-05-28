package com.studyquiz.ratemovie.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyquiz.ratemovie.models.Movie
import com.studyquiz.ratemovie.models.MovieListResponse
import com.studyquiz.ratemovie.repository.MovieRepository
import com.studyquiz.ratemovie.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor (
    var mContext: Context,
    var movieRepository: MovieRepository
    ) : ViewModel() {

    var movieResponseResource: MutableLiveData<Resource<MovieListResponse>> = MutableLiveData()
    var page = 1
    var allMovieResponse: MovieListResponse? = null



    init {
        getMoviesList("en-US")
    }

    fun getMoviesList(countryCode: String) = viewModelScope.launch {
        safeAllMoviesCall(countryCode)
    }

    private suspend fun safeAllMoviesCall(countryCode: String) {
        movieResponseResource.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = movieRepository.getMoviesList(countryCode, page)
                movieResponseResource.postValue(handleAllMoviesResponse(response))
            } else {
                movieResponseResource.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> movieResponseResource.postValue(Resource.Error("Network Failure"))
                else -> movieResponseResource.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleAllMoviesResponse(response: Response<MovieListResponse>) : Resource<MovieListResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                if(allMovieResponse == null) {
                    allMovieResponse = resultResponse
                } else {
                    val oldMovies = allMovieResponse?.movies
                    val newMovies = resultResponse.movies
                    if (newMovies != null) {
                        oldMovies?.addAll(newMovies)
                    }
                }
                return Resource.Success(allMovieResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }





    fun saveRatedMovie(movie: Movie) = viewModelScope.launch {
        movieRepository.upsert(movie)
    }

    fun getSavedRatedMovies() = movieRepository.getSavedRatedMovies()



    private fun hasInternetConnection(): Boolean {

        //val connectivityManager = getApplication<MovieApplication>().getSystemService(
        val connectivityManager = mContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}












