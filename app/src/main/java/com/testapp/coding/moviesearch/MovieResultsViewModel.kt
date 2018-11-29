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
    private var mNetworkRequests: NetworkRequests = NetworkRequests()

    private lateinit var mMovieSearchResults: MutableLiveData<MovieSearchDTO>
    private val mMovieIntermediateSearchResults = MutableLiveData<MovieSearchDTO>()

    /**
     * Fetches movie results from the api. As soon as the network call executes the model updates the ViewModel and the
     * ViewModel in-turn updates the View
     * @return LiveData on which the view would observe on
     */
    fun getMovieResults(): LiveData<MovieSearchDTO> {
        if (!::mMovieResults.isInitialized) {
            val movieLiveData = mNetworkRequests.fetchInitialMovieSearchResults()
            mMovieResults = Transformations.switchMap(movieLiveData) { movieSearchDTO ->
                mMovieIntermediateResults.value = movieSearchDTO
                mMovieIntermediateResults
            } as MutableLiveData<MovieSearchDTO>
        }
        return mMovieResults
    }

    /**
     * Returns LiveData for movie search results that the view can observe on
     */
    fun getMovieSearchResults(): LiveData<MovieSearchDTO> {
        if (!::mMovieSearchResults.isInitialized) {
            val movieLiveData = mNetworkRequests.fetchMovieSearchResults()
            mMovieSearchResults = Transformations.switchMap(movieLiveData) { movieSearchDTO ->
                mMovieIntermediateSearchResults.value = movieSearchDTO
                mMovieIntermediateSearchResults
            } as MutableLiveData<MovieSearchDTO>
        }
        return mMovieSearchResults
    }

    /**
     * Makes a call to get results based on users query
     * @param query searched query by the user
     */
    fun makeMovieSearchResultsCall(query: String) {
        mNetworkRequests.getMovieSearchResults(query)
    }
}