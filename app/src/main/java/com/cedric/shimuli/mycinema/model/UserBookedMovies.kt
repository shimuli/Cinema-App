package com.cedric.shimuli.mycinema.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserBookedMovies: Serializable {

    @SerializedName("id")
    var id: Int? = 1

    @SerializedName("movieName")
    var movieName: String? = "movieName"

    @SerializedName("cusomerName")
    var cusomerName: String? = "cusomerName"

    @SerializedName("quantity")
    var quantity: Int? = null

    @SerializedName("price")
    var price: Double? = null

    @SerializedName("playingDate")
    var playingDate: String? = "playingDate"

    @SerializedName("playTime")
    var playTime: String? = "playTime"

    @SerializedName("movieImage")
    var movieImage: String? = "movieImage"

    @SerializedName("watched")
    var watched: Boolean? =false



   /*
    "cusomerName": "Lorna Kerubo",
    "reservationTime": "2021-09-02T00:00:00",
    "email": "kerry@gmail.com",
    "quantity": 30,
    "price": 1500,
    "totalCost": 45000,
    "playingDate": "2021-08-29T00:00:00",
    "playTime": "2021-08-17T17:45:00",
    "watched": false,
    "movieImage": "https://localhost:44353\\1f52394f-8f44-437c-b24a-43b0f3a1a4ca.jpg"*/
}