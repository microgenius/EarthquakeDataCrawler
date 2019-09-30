package com.sample.crawler.earthquake.web.api.dataprovider

import com.sample.crawler.earthquake.web.api.dataprovider.type.IDataProvider
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component

@Component
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
class DataProviderContext {
    private val dataProviderList : MutableList<IDataProvider>

    init {
        this.dataProviderList = ArrayList()
    }

    fun addDataProvider(dataProvider: IDataProvider) = this.dataProviderList.add(dataProvider)

    fun startDataFetching() {
        this.dataProviderList.parallelStream()
                .forEach {
                    this.scheduleDataProviderTask(0, 0, it)
                }
    }

    private fun scheduleDataProviderTask(waitTime: Long, executedCount: Int, dataProvider: IDataProvider) {
        val toleranceParameters = dataProvider.providerToleranceParameters()
        if (executedCount >= toleranceParameters.reTryCount)
            return

        // TODO ST: Change with proper tolerance process
        Thread.sleep(waitTime)

        dataProvider.initializeCallParameters()
        val callSuccessful = dataProvider.makeCall()

        if (!callSuccessful) {
            val incrementedTryCount = executedCount + 1
            this.scheduleDataProviderTask(toleranceParameters.waitUntilReTry, incrementedTryCount, dataProvider)
        }
    }
}