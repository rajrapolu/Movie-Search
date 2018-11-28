package com.testapp.coding.moviesearch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.testapp.coding.moviesearch.models.MovieSearchDTO
import com.testapp.coding.moviesearch.networkcalls.NetworkRequests

class MovieResultsViewModel : ViewModel() {

    private lateinit var mMovieResults: MutableLiveData<MovieSearchDTO>
    private val mMovieIntermediateResults = MutableLiveData<MovieSearchDTO>()
    private lateinit var mNetworkRequests: NetworkRequests

    /**
     * Fetches movie results from the api. As soon as the network call executes the model updates the ViewModel and the
     * ViewModel in-turn updates the View
     * @return LiveData on which the view would observe on
     */
    fun getMovieResults(): LiveData<MovieSearchDTO> {
        if (!::mMovieResults.isInitialized) {
            mNetworkRequests = NetworkRequests()
            val movieLiveData = mNetworkRequests.fetchInitialMovieSearchResults()
            mMovieResults = Transformations.switchMap(movieLiveData) { movieSearchDTO ->
                mMovieIntermediateResults.value = movieSearchDTO
                mMovieIntermediateResults
            } as MutableLiveData<MovieSearchDTO>
        }
        return mMovieResults
    }
}