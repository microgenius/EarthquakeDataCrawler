package com.sample.crawler.earthquake.web.api.dataprovider.type

import com.sample.crawler.earthquake.dto.provider.ToleranceParameters

interface IDataProvider {
    fun initializeCallParameters()

    fun makeCall() : Boolean

    fun providerToleranceParameters() : ToleranceParameters
}