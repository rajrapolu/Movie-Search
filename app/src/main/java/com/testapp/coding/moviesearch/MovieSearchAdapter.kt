package com.testapp.coding.moviesearch

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testapp.coding.moviesearch.models.MovieInfoDTO
import com.testapp.coding.moviesearch.networkcalls.GlideApp
import kotlinx.android.synthetic.main.movie_item_details.view.*

private const val TAG = "MovieSearchAdapter"

class MovieSearchAdapter(
    private val mMovieResults: MutableList<MovieInfoDTO>,
    private val context: Context
) :
    RecyclerView.Adapter<MovieSearchAdapter.MovieSearchViewHolder>() {

    // Interface that needs to implemented by the activities that use this adapter
    interface ItemClickListener {
        fun onItemClicked(movieInfoDTO: MovieInfoDTO)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSearchViewHolder {
        return MovieSearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mMovieResults.size
    }

    override fun onBindViewHolder(holder: MovieSearchViewHolder, position: Int) {
        holder.bind(mMovieResults[position])
    }

    // Updates the adapter items
    fun updateItems(results: List<MovieInfoDTO>?, clearItem: Boolean) {
        results?.let {
            // When a new search is performed we need to clear the existing data otherwise just add the contents to the
            // existing one
            if (clearItem) {
                mMovieResults.clear()
                mMovieResults.addAll(results)
                notifyDataSetChanged()
            } else {
                val size = mMovieResults.size
                mMovieResults.addAll(results)
                notifyItemRangeInserted(size, mMovieResults.size - size)
            }
        }
    }

    inner class MovieSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieInfo: MovieInfoDTO?) {

            movieInfo?.let {
                GlideApp.with(context).load("${ConstantsClass.IMAGES_BASE_URL}${movieInfo.imageUrl}")
                    .placeholder(R.mipmap.ic_launcher).into(itemView.movie_item_image_view)
                itemView.apply {
                    movie_item_title.text = movieInfo.title
                    movie_item_release_date.text = context.getString(
                        R.string.release_date_value,
                        DateUtil.formatReleaseDate(movieInfo.releaseDate)
                    )

                    setOnClickListener { _ ->
                        try {
                            (context as ItemClickListener).onItemClicked(movieInfo)
                        } catch (e: ClassCastException) {
                            Log.e(TAG, "Activity should implement ItemClickListener")
                        }
                    }
                }
            }
        }
    }
}