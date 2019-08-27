package com.minghan.lomotif.media.api

import com.minghan.lomotif.media.data.Image
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("api?key=10961674-bf47eb00b05f514cdd08f6e11")
    fun getImages(
        @Query("page") page: Int
    ): Call<Image.ImageJsonResponse>
}
