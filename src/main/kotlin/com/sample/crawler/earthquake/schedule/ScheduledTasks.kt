package com.sample.crawler.earthquake.schedule

import com.sample.crawler.earthquake.base.LoggableClass
import com.sample.crawler.earthquake.web.api.dataprovider.DataProviderContext
import com.sample.crawler.earthquake.web.api.dataprovider.type.IDataProvider
import com.sample.crawler.earthquake.web.crawler.WebCrawlerContext
import com.sample.crawler.earthquake.web.crawler.types.IWebCrawler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author tanriverdi
 */
@Component
class ScheduledTasks(private val webCrawlerContext: WebCrawlerContext,
                     private val dataProviderContext: DataProviderContext,
                     @Qualifier("AFADDataProvider")
                     private val AFADDataProvider: IDataProvider,
                     @Qualifier("kandilliCrawler")
                     private val kandilliWebCrawler: IWebCrawler) : LoggableClass() {

    init {
        this.addEarthquakeDataCenterParametersToContext()
        this.addDataProvidersToContext()
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

    private fun addDataProvidersToContext() = this.dataProviderContext.addDataProvider(AFADDataProvider)

    @Scheduled(cron = "0 0/30 * * * ?")
    fun scheduleDataCenterJob() = this.webCrawlerContext.startCrawling()

    @Scheduled(cron = "0 10/30 * * * ?")
    fun scheduleDataProviderJob() = this.dataProviderContext.startDataFetching()
}
