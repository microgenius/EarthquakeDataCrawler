package com.sample.crawler.earthquake.web.crawler

import com.sample.crawler.earthquake.web.crawler.types.IWebCrawler

import java.net.URL

class WebCrawlerPair(var contextOwner: IWebCrawler? = null, var contextSourceUrl: URL? = null) {
}
