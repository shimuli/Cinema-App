package com.cedric.shimuli.mycinema.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BookingResponse : Serializable {
    @SerializedName("reservationTime")
    var reservationTime: String? = "reservationTime"
}