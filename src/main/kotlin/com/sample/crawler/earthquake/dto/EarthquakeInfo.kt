package com.sample.crawler.earthquake.dto

import java.util.*

class EarthquakeInfo {
    var earthquakeTime: Date? = null
    var earthquakeLocation: Location? = null
    /** This field will be represent as KM for now  */
    var depthInfo: Double = 0.toDouble()
    var intensityOfEarthquake: Double = 0.toDouble()
    var solutionQuality: String? = null
    var updatedTime: Date? = null
    var dataProvider: String = ""
}
