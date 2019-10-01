package com.sample.crawler.earthquake.web.api.dataprovider

import com.sample.crawler.earthquake.web.api.dataprovider.type.IDataProvider
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import java.util.*

@Component
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
class DataProviderContext(private val taskScheduler: TaskScheduler) {
    private val dataProviderList: MutableList<IDataProvider>

    init {
        this.dataProviderList = ArrayList()
    }

    fun addDataProvider(dataProvider: IDataProvider) = this.dataProviderList.add(dataProvider)

    fun startDataFetching() {
        this.dataProviderList.parallelStream()
                .forEach {
                    val now = Date()
                    taskScheduler.schedule({
                        this.scheduleDataProviderTask(0, it)
                    }, now)
                }
    }

    private fun scheduleDataProviderTask(executedCount: Int, dataProvider: IDataProvider) {
        val toleranceParameters = dataProvider.providerToleranceParameters()
        if (executedCount >= toleranceParameters.reTryCount)
            return

        dataProvider.initializeCallParameters()
        val callSuccessful = dataProvider.makeCall()

        if (!callSuccessful) {
            val incrementedTryCount = executedCount + 1
            val eventTime = Date(System.currentTimeMillis() + toleranceParameters.waitUntilReTry)

            // Schedule Task to eventTime
            taskScheduler.schedule({
                this.scheduleDataProviderTask(incrementedTryCount, dataProvider)
            }, eventTime)
        }
    }
}