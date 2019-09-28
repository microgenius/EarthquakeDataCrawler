package com.sample.crawler.earthquake.data.parser

import com.sample.crawler.earthquake.data.parser.type.IDataParser
import com.sample.crawler.earthquake.dto.EarthquakeInfo
import org.springframework.stereotype.Component
import java.util.*

@Component("kandilliDataParser")
class KandilliDataParser : IDataParser<List<EarthquakeInfo>> {
    override fun parseData(rawData: String?): List<EarthquakeInfo> {
        if (rawData?.length ?: 0 <= 0)
            return Collections.emptyList()

        return rawData!!.lines()
                .drop(6)
                .map { line -> this.parseLineData(line) }
                .filter { data -> Objects.nonNull(data) }
                .map { data -> data!! }
    }

    private fun parseLineData(lineData: String) : EarthquakeInfo? {
        val elements = lineData
                .split("  ")
                .filter { data -> data.isNotBlank() }
                .map { data -> data.trim() }

        return EarthquakeInfo.parse(elements)
    }
}
