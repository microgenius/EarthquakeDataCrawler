package com.sample.crawler.earthquake.dto

import com.sample.crawler.earthquake.base.CommonUtils
import java.time.LocalDateTime

class EarthquakeInfo {
    var earthquakeTime: LocalDateTime? = null
    var earthquakeLocation: Location? = null
    /** This field will be represent as KM for now  */
    var depthInfo: Double = 0.toDouble()
    var intensityOfEarthquake: Double = 0.toDouble()
    var solutionQuality: String? = null
    var updatedTime: LocalDateTime? = null

    companion object {
        val INDX_EARTHQUAKE_TIME: Int = 0
        val INDX_LATITUDE: Int = 1
        val INDX_LONGITUDE: Int = 2
        val INDX_DEPTH: Int = 3
        val INDX_INTENSITY: Int = 5
        val INDX_LOC_DESC: Int = 7
        val INDX_SLTN_QUALITY: Int = 8
        val INDX_UPDATE_TIME: Int = 9

        fun parse(value: List<String>) : EarthquakeInfo {
            val earthquakeInfo = EarthquakeInfo()

            val earthquakeTimeString = value[INDX_EARTHQUAKE_TIME]
            earthquakeInfo.earthquakeTime = CommonUtils.safeConvertDatetime(earthquakeTimeString)

            val earthquakeLocation = Location()
            earthquakeLocation.latitude = value[INDX_LATITUDE].toDouble()
            earthquakeLocation.longitude = value[INDX_LONGITUDE].toDouble()
            earthquakeLocation.locationDescription = value[INDX_LOC_DESC]
            earthquakeInfo.earthquakeLocation = earthquakeLocation

            earthquakeInfo.depthInfo = value[INDX_DEPTH].toDouble()
            earthquakeInfo.intensityOfEarthquake = value[INDX_INTENSITY].toDouble()
            earthquakeInfo.solutionQuality = value[INDX_SLTN_QUALITY]
            if (value.size > INDX_UPDATE_TIME) {
                val earthquakeInfoUpdateTime = value[INDX_UPDATE_TIME]
                earthquakeInfo.updatedTime = CommonUtils.safeConvertDatetime(earthquakeInfoUpdateTime)
            }

            return earthquakeInfo
        }
    }
}
