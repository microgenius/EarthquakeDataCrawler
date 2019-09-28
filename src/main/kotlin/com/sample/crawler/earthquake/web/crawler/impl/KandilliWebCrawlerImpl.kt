package com.sample.crawler.earthquake.web.crawler.impl

import com.sample.crawler.earthquake.data.parser.KandilliDataParser
import com.sample.crawler.earthquake.data.parser.type.IDataParser
import com.sample.crawler.earthquake.dto.EarthquakeInfo
import com.sample.crawler.earthquake.web.crawler.types.IWebCrawler
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.net.URL

@Component("kandilliCrawler")
class KandilliWebCrawlerImpl : IWebCrawler {
    @Autowired
    @Qualifier("kandilliDataParser")
    private lateinit var earthquakeDataParser : KandilliDataParser

    private val DATA_SELECT_QUERY = "html body pre"
    private val EARTHQUAKE_DATA_SOURCE_URL : String = "http://www.koeri.boun.edu.tr/scripts/lst4.asp"

    override fun shouldVisitPage(pageUrl: URL?): Boolean {
        return EARTHQUAKE_DATA_SOURCE_URL == pageUrl?.toString()
    }

    override fun afterVisit(visitedPage: Document) {
        val rawData = visitedPage.select(DATA_SELECT_QUERY)?.text()
        val parsedData = earthquakeDataParser.parseData(rawData)
    }
}
