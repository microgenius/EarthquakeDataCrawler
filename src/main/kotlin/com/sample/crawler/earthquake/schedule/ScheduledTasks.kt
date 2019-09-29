package com.sample.crawler.earthquake.schedule

import com.sample.crawler.earthquake.base.LoggableClass
import com.sample.crawler.earthquake.web.crawler.WebCrawlerContext
import com.sample.crawler.earthquake.web.crawler.impl.KandilliWebCrawlerImpl
import com.sample.crawler.earthquake.web.crawler.types.IWebCrawler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author tanriverdi
 */
@Component
class ScheduledTasks(private val webCrawlerContext: WebCrawlerContext,
                     @Qualifier("kandilliCrawler")
                     private val kandilliWebCrawler: IWebCrawler) : LoggableClass() {

    init {
        this.addEarthquakeDataCenterParametersToContext()
    }

    private fun addEarthquakeDataCenterParametersToContext() {
        val urlString = "http://www.koeri.boun.edu.tr/scripts/lst4.asp"
        val isSourceUrlAddedToContext = this.webCrawlerContext.addSourceUrl(urlString)
        if (!isSourceUrlAddedToContext) {
            this.logger.error("Source Url could not be added to crawler context, source URL: {}", urlString)
            return
        }
        this.webCrawlerContext.addCrawler(kandilliWebCrawler)
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    fun scheduleDataCenterJob() {
        this.webCrawlerContext.startCrawling()
    }
}
