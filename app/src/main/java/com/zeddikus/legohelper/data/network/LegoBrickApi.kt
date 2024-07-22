package com.zeddikus.legohelper.data.network


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LegoBrickApi {
    @GET("/catalogItemInv.asp")
    suspend fun getStartData(@Query("S") text: String): Response<ResponseBody>
}
