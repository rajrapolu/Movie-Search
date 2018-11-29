package com.testapp.coding.moviesearch.models

import com.google.gson.annotations.SerializedName

data class MovieInfoDTO(
    val id: Int,
    val title: String, @SerializedName("release_date") val releaseDate: String, @SerializedName("poster_path") val imageUrl: String
)