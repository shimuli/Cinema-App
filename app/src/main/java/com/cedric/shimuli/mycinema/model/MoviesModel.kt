package com.cedric.shimuli.mycinema.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MoviesModel: Serializable {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("name")
    var name: String? = "name"

    @SerializedName("playDate")
    var playDate: String? = "playDate"

    @SerializedName("playTime")
    var playTime: String? = "playTime"

    @SerializedName("language")
    var language: String? = "language"

    @SerializedName("rating")
    var rating: String? = "rating"

    @SerializedName("genre")
    var genre: String? = "genre"

    @SerializedName("imageurl")
    var imageurl: String? = "imageurl"

    @SerializedName("description")
    var description: String? = "description"

    @SerializedName("ticketPrice")
    var ticketPrice: Double? = null

    @SerializedName("trailer")
    var trailer: String? = "trailer"

    @SerializedName("duration")
    var duration: String? = "duration"

}
