package com.sample.crawler.earthquake.web.api.repository

import AFADLatestEarthquakeResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface AFADAPICallRepository {
    @POST("/latestCatalogsList")
    fun fetchLatestEarthquakeData(@Query("m") magnitude : Int,
                                  @Query("utc") isUtcTime : Int,
                                  @Query("lastDay") lastDay : Int)
            : Call<List<AFADLatestEarthquakeResponse>>
}