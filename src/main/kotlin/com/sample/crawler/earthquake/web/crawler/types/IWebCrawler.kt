package com.sample.crawler.earthquake.web.crawler.types

import org.jsoup.nodes.Document

import java.net.URL

interface IWebCrawler {
    fun shouldVisitPage(pageUrl: URL?): Boolean

    fun afterVisit(visitedPage: Document)
}
