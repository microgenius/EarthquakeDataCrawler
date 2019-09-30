package com.sample.crawler.earthquake.service.earthquake.type

import com.sample.crawler.earthquake.dto.EarthquakeInfo

interface IEarthquakeInfoService {
    fun upsertDocuments(providerName: String, convertedData: List<EarthquakeInfo>)

    fun getLastUpdatedData(providerName: String): EarthquakeInfo?

    fun getLastDataForProvider(providerName: String) : EarthquakeInfo?

    fun insert(bulkData: List<EarthquakeInfo>)

    fun update(bulkData: List<EarthquakeInfo>)
}