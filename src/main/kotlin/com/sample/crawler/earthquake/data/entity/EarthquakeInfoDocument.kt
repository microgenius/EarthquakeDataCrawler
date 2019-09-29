package com.sample.crawler.earthquake.data.entity

import com.sample.crawler.earthquake.data.entity.base.IdentityDocument
import com.sample.crawler.earthquake.dto.Location
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("EarthquakeInfo")
class EarthquakeInfoDocument : IdentityDocument<Long>() {
    var earthquakeTime: Date? = null
    var earthquakeLocation: Location? = null
    var depthInfo: Double = 0.toDouble()
    var intensity: Double = 0.toDouble()
    var solutionQuality: String? = null
    var updatedTime: Date? = null
    var dataProvider: String = ""
    @Version
    var version: String = "1"
}
