package com.testapp.coding.moviesearch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testapp.coding.moviesearch.models.MovieInfoDTO
import com.testapp.coding.moviesearch.networkcalls.GlideApp
import kotlinx.android.synthetic.main.movie_item_details.view.*

class MovieSearchAdapter(private val mMovieResults: MutableList<MovieInfoDTO> = mutableListOf()) :
    RecyclerView.Adapter<MovieSearchAdapter.MovieSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSearchViewHolder {
        return MovieSearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mMovieResults.size
    }

    override fun onBindViewHolder(holder: MovieSearchViewHolder, position: Int) {
        holder.bind(mMovieResults[position])
    }

    fun updateItems(results: List<MovieInfoDTO>?) {
        results?.let {
            mMovieResults.addAll(results)
            notifyDataSetChanged()
        }
    }

    class MovieSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieInfo: MovieInfoDTO?) {
            movieInfo?.let {
                GlideApp.with(itemView.context).load("${ConstantsClass.IMAGES_BASE_URL}${movieInfo.imageUrl}")
                    .placeholder(R.mipmap.ic_launcher).into(itemView.movie_item_details_image_view)
                itemView.movie_details_title.text = movieInfo.title
            }
        }
    }
}