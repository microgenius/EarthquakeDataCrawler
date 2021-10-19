package com.sample.crawler.earthquake.web.crawler

import com.sample.crawler.earthquake.web.crawler.types.IWebCrawler
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * @author tanriverdi
 */
@Component
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
class WebCrawlerContext {
    private val DEFAULT_TIMEOUT = 15 * 1000

    private val crawlerList: MutableList<IWebCrawler>
    private val sourceUrlList: MutableList<URL>

    init {
        this.crawlerList = ArrayList()
        this.sourceUrlList = ArrayList()
    }

    fun addCrawler(crawler: IWebCrawler) = this.crawlerList.add(crawler)

    fun addCrawler(callableCrawler: Supplier<IWebCrawler>) = this.addCrawler(callableCrawler.get())

    fun addSourceUrl(sourceUrl: URL) = this.sourceUrlList.add(sourceUrl)

    fun addSourceUrl(sourceUrlString: String): Boolean {
        return try {
            val sourceUrl = URL(sourceUrlString)
            this.addSourceUrl(sourceUrl)
            true
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            false
        }
    }

    fun startCrawling() {
        val crawlerPairs = this.makePairForCrawling()
        crawlerPairs.parallelStream()
                .forEach { crawlerPair ->
                    this.fetchDataFromUrl(crawlerPair.contextSourceUrl)
                            .ifPresent { fetchedDocument -> crawlerPair.contextOwner?.afterVisit(fetchedDocument) }
                }
    }

    private fun fetchDataFromUrl(sourceUrl: URL): Optional<Document> {
        return try {
            Optional.ofNullable(Jsoup.parse(sourceUrl, DEFAULT_TIMEOUT))
        } catch (e: IOException) {
            e.printStackTrace()
            Optional.empty()
        }
    }

    private fun makePairForCrawling(): List<WebCrawlerPair> {
        return this.sourceUrlList.parallelStream()
                .map { sourceUrl ->
                    this.crawlerList.parallelStream()
                            .filter { crawler -> crawler.shouldVisitPage(sourceUrl) }
                            .map { eligibleCrawler -> WebCrawlerPair(eligibleCrawler, sourceUrl) }
                            .collect(Collectors.toList())
                }
                .flatMap { bulkData -> bulkData.parallelStream() }
                .collect(Collectors.toList())
    }
}
