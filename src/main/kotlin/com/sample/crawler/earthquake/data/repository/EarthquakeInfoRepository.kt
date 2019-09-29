package com.sample.crawler.earthquake.data.repository

import com.sample.crawler.earthquake.data.entity.EarthquakeInfoDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface EarthquakeInfoRepository : MongoRepository<EarthquakeInfoDocument, Long> {
    fun findAllByUpdatedTimeIsNotNullOrderByUpdatedTimeDesc(): List<EarthquakeInfoDocument>

    fun findTop1ByOrderByEarthquakeTimeDesc() : EarthquakeInfoDocument?
}