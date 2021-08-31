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

}

//"id": 1007,
//"name": "Jumanji",
//"duration": "1 hrs 59 min",
//"language": "Unniversal",
//"rating": 7.9,
//"genre": "Actin, Adventure,dramma",
//"imageurl": "https://localhost:44353\\acce744c-933c-4de9-950b-b6b934193495.jpg"
//"playDate": "2021-08-31T00:00:00",
//"playTime": "2021-08-18T15:45:00",