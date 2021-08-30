package com.cedric.shimuli.mycinema.model

import com.google.gson.annotations.SerializedName

class LoginResponse {

    @SerializedName("message")
    var message: String? = "message"

    @SerializedName("user_id")
    var userId: Int? = 1

    @SerializedName("access_token")
    var accessToken: String? = "message"

    @SerializedName("user_name")
    var userName: String? = "user_name"

    @SerializedName("user_phone")
    var userPhone: String? = "user_phone"


//    "message": "Login was successful",
//    "user_id": 4,
//    "user_name": "Cedric Shimuli",
//    "user_phone": "+254716591180",
//    "user_email": "shimulicedric@gmail.com",
//    "isPhoneVerified": true,
//    "isEmailVerified": true,
//    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiIrMjU0NzE2NTkxMTgwIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbW9iaWxlcGhvbmUiOiIrMjU0NzE2NTkxMTgwIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9yb2xlIjoiQWRtaW4iLCJuYmYiOjE2MzAwMDQ0MzQsImV4cCI6MTYzMDA5MDgzNCwiaXNzIjoibG9jYWxob3N0LmNvbSIsImF1ZCI6ImxvY2FsaG9zdC5jb20ifQ.WtK8qe8E77qZNUeT1VQCNqattYvD8L0qBqwQdavap-I",
//    "token_type": "bearer"
}