package com.example.koronaapp.network
import com.example.koronaapp.Model.HospitalInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface HospitalContents {
    @GET(value = "v1/centers")
    fun searchhaspital(
        @Query(value = "page", encoded = true) page: String,
        @Query(value = "perPage", encoded = true) perPage: String,
        @Query(value = "serviceKey", encoded = true) serviceKey: String,
    ): Call<HospitalInfoResponse>
}