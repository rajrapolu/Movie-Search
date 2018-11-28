package com.testapp.coding.moviesearch.networkcalls

import android.arch.lifecycle.MutableLiveData
import com.testapp.coding.moviesearch.ConstantsClass
import com.testapp.coding.moviesearch.models.MovieSearchDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkRequests {

    private lateinit var mMovieNetworkResults: MutableLiveData<MovieSearchDTO>

    fun fetchInitialMovieSearchResults(): MutableLiveData<MovieSearchDTO> {
        if (!::mMovieNetworkResults.isInitialized) {
            mMovieNetworkResults = MutableLiveData()
            discoverMovies(1)
        }
        return mMovieNetworkResults
    }

    /**
     * Fetches movies in ascending order of release dates
     */
    private fun discoverMovies(page: Int) {
        MovieRetrofitService.create()
            .discoverMovies(
                MovieRetrofitService.API_KEY,
                ConstantsClass.DISCOVER_MOVIES_LANGUAGE,
                ConstantsClass.DISCOVER_MOVIES_SORT_BY,
                false,
                false,
                page
            ).enqueue(object : Callback<MovieSearchDTO> {
                override fun onFailure(call: Call<MovieSearchDTO>, t: Throwable) {
                    mMovieNetworkResults.value = null
                }

                override fun onResponse(call: Call<MovieSearchDTO>?, response: Response<MovieSearchDTO>?) {
                    if (response != null && response.isSuccessful) {
                        mMovieNetworkResults.value = response.body()
                    }
                }

            })
    }

    /**
     * Fetches movie results from the api
     */
    private fun fetchMovieResults() {
        MovieRetrofitService.create().searchMovies(MovieRetrofitService.API_KEY, "football", 1).enqueue(object :
            Callback<MovieSearchDTO> {
            override fun onFailure(call: Call<MovieSearchDTO>, t: Throwable) {
                mMovieNetworkResults.value = null
            }

            override fun onResponse(call: Call<MovieSearchDTO>?, response: Response<MovieSearchDTO>?) {
                if (response != null && response.isSuccessful) {
                    mMovieNetworkResults.value = response.body()
                }
            }
        })
    }
}