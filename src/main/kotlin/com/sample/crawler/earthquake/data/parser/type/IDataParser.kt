package com.sample.crawler.earthquake.data.parser.type

interface IDataParser<TargetData> {
    fun parseData(rawData: String?) : TargetData?
}