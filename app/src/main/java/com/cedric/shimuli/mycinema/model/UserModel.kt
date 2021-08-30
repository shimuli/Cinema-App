package com.cedric.shimuli.mycinema.model

import com.google.gson.annotations.SerializedName

class UserModel {
    @SerializedName("name")
    var name: String? = "name"

    @SerializedName("phone")
    var phone: String? = "phone"

    @SerializedName("phoneVerified")
    var phoneVerified: String? = "phoneVerified"


//    "id": 1,
//    "name": "Peter Graffin",
//    "email": "steve@familyguy.com",
//    "phone": "0784536151",
//    "role": "User",
//    "imageUrl": "https://moviessystem.azurewebsites.net\\1bab60e2-89ea-4217-b73f-dd9bd2f41184.png",
//    "phoneVerified": false
}