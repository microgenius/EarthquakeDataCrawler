@file:Suppress("PropertyName")

package com.sample.crawler.earthquake.data.parser

import com.sample.crawler.earthquake.base.CommonUtils
import com.sample.crawler.earthquake.base.LoggableClass
import com.sample.crawler.earthquake.data.parser.type.IDataParser
import com.sample.crawler.earthquake.dto.EarthquakeInfo
import com.sample.crawler.earthquake.dto.Location
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.*

@Component("kandilliDataParser")
class KandilliDataParser : LoggableClass(), IDataParser<List<EarthquakeInfo>> {
    val PROVIDER_NAME = "Boğaziçi Üniversitesi Kandilli Rasathanesi"
    val INDX_EARTHQUAKE_TIME: Int = 0
    val INDX_LATITUDE: Int = 1
    val INDX_LONGITUDE: Int = 2
    val INDX_DEPTH: Int = 3
    val INDX_INTENSITY: Int = 5
    val INDX_LOC_DESC: Int = 7
    val INDX_SLTN_QUALITY: Int = 8
    val INDX_UPDATE_TIME: Int = 9

    override fun parseData(rawData: String?): List<EarthquakeInfo> {
        if (rawData?.length ?: 0 <= 0)
            return Collections.emptyList()

        return rawData!!.lines()
                .drop(6)
                .map { this.parseLineData(it) }
                .filter { Objects.nonNull(it) }
                .map { it!! }
    }

    private fun parseLineData(lineData: String): EarthquakeInfo? {
        try {
            val elements = lineData
                    .split("  ")
                    .filter { it.isNotBlank() }
                    .map { it.trim() }

            val earthquakeInfo = EarthquakeInfo()
            earthquakeInfo.dataProvider = PROVIDER_NAME

            val earthquakeTimeString = elements[INDX_EARTHQUAKE_TIME]
            earthquakeInfo.earthquakeTime = CommonUtils.safeConvertDatetime(earthquakeTimeString)

            val earthquakeLocation = Location()
            earthquakeLocation.latitude = elements[INDX_LATITUDE].toDouble()
            earthquakeLocation.longitude = elements[INDX_LONGITUDE].toDouble()
            earthquakeLocation.locationDescription = elements[INDX_LOC_DESC]
            earthquakeInfo.earthquakeLocation = earthquakeLocation

            earthquakeInfo.depthInfo = elements[INDX_DEPTH].toDouble()
            earthquakeInfo.intensityOfEarthquake = elements[INDX_INTENSITY].toDouble()
            earthquakeInfo.solutionQuality = elements[INDX_SLTN_QUALITY]
            if (elements.size > INDX_UPDATE_TIME) {
                val earthquakeInfoUpdateTime = elements[INDX_UPDATE_TIME]
                earthquakeInfo.updatedTime = CommonUtils.safeConvertDatetime(earthquakeInfoUpdateTime)
            }

            return earthquakeInfo
        } catch (e : Exception) {
            this.logger.error("An Error has occurred while parsing line data, Line Data: {}, Detail: ", lineData, e)
            return null
        }
    }
}
