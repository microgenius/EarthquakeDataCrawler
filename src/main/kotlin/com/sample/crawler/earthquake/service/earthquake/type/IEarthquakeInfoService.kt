package com.sample.crawler.earthquake.service.earthquake.type

import com.sample.crawler.earthquake.dto.EarthquakeInfo

interface IEarthquakeInfoService {
    fun upsertInfoToDB(info: EarthquakeInfo): Boolean

    fun getLastInsertedData(): EarthquakeInfo?

    fun getLastUpdatedData(): EarthquakeInfo?

    fun insert(bulkData: List<EarthquakeInfo>)

    fun update(bulkData: List<EarthquakeInfo>)
}