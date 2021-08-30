package com.cedric.shimuli.mycinema.model

import com.google.gson.annotations.SerializedName

class RegisterResponse {
    @SerializedName("message")
    var message: String? = "message"

    @SerializedName("id")
    var id: Int? = 1

    @SerializedName("user_name")
    var userName: String? = "name"

    @SerializedName("user_email")
    var userEmail: String? = "email"

    @SerializedName("verifuPhoneCode")
    var verifyPhoneCode: String? = "verify"

}