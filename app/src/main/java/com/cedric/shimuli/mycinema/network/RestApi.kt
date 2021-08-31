package com.cedric.shimuli.kotlincrud.network

import android.graphics.Bitmap
import com.cedric.shimuli.mycinema.model.LoginResponse
import com.cedric.shimuli.mycinema.model.MoviesModel
import com.cedric.shimuli.mycinema.model.RegisterResponse
import com.cedric.shimuli.mycinema.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface RestApi {
    @FormUrlEncoded
    @POST("Users/Register")
    fun register(
        @Field("Name") Name: String,
        @Field("Email") Email: String,
        @Field("Phone") Phone: String,
        @Field("Password") Password: String,
    ): Call<RegisterResponse?>?

    @FormUrlEncoded
    @POST("Users/VerfiyPhone/{userId}")
    fun verifyNumber(
        @Path("userId") userId:String,
        @Field("Phone") Phone: String,
        @Field("confirmCode") confirmCode: String,
    ): Call<RegisterResponse?>?

    @FormUrlEncoded
    @POST("Users/Login")
    fun loginIn(
        @Field("Phone") Phone: String,
        @Field("Password") Password: String,
    ): Call<LoginResponse?>?


    @GET("Users/userDetails")
    fun getProfile(
        @Header("Authorization") authToken: String?,
        @Query("userId") userId: String
    ):Call<UserModel?>?


    @GET("Movies/AllMovies")  // https://localhost:44353/api/Movies/searchMovie?query=i
    fun fetchMovies(
        @Header("Authorization") authToken: String?,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sort") sort: String,

    ): Call<List<MoviesModel>>

    @GET("Movies/searchMovie")
    fun searchMovie(
        @Header("Authorization") authToken: String?,
        @Query("query") query: String
    ): Call<List<MoviesModel>>

    @GET("Movies/Todaymovies")
    fun todayMovies(
        @Header("Authorization") authToken: String?,
    ): Call<List<MoviesModel>>


//    @POST("fetchcategory_products")
//    fun getProductCategory(
//        @Header("Authorization") authToken: String?,
//        @Query("category_id") category_id: Int
//    ): Call<List<Product?>?>?

    /**
     * This method will allow us update our mysql data by making a HTTP POST request.
     * After that
     * we will receive a ResponseModel model object
     */
//    @FormUrlEncoded
//    @POST("index.php")
//    fun updateData(
//        @Field("action") action: String?,
//        @Field("id") id: String?,
//        @Field("name") name: String?,
//        @Field("description") description: String?,
//        @Field("galaxy") galaxy: String?,
//        @Field("star") star: String?,
//        @Field("dob") dob: String?,
//        @Field("died") died: String?
//    ): Call<RegisterResponse?>?

    /**
     * This method will allow us to search our data while paginating the search results. We
     * specify the search and pagination parameters as fields.
     */
//    @FormUrlEncoded
//    @POST("index.php")
//    fun search(
//        @Field("action") action: String?,
//        @Field("query") query: String?,
//        @Field("start") start: String?,
//        @Field("limit") limit: String?
//    ): Call<ResponseModel?>?

    /**
     * This method will allow us to remove or delete from database the row with the
     * specified
     * id.
     */
//    @FormUrlEncoded
//    @POST("index.php")
//    fun remove(
//        @Field("action") action: String?,
//        @Field("id") id: String?
//    ): Call<ResponseModel?>?
}