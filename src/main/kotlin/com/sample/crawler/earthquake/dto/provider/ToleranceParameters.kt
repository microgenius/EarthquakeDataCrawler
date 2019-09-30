package com.sample.crawler.earthquake.dto.provider

class ToleranceParameters {
    var reTryCount: Int = 1
    var waitUntilReTry : Long = (20 * 1000)
}