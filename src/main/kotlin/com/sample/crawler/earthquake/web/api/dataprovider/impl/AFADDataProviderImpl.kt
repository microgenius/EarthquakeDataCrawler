package com.sample.crawler.earthquake.web.api.dataprovider.impl

import AFADLatestEarthquakeResponse
import com.sample.crawler.earthquake.base.CommonUtils
import com.sample.crawler.earthquake.base.Constants
import com.sample.crawler.earthquake.base.LoggableClass
import com.sample.crawler.earthquake.dto.EarthquakeInfo
import com.sample.crawler.earthquake.dto.Location
import com.sample.crawler.earthquake.dto.provider.ToleranceParameters
import com.sample.crawler.earthquake.service.earthquake.type.IEarthquakeInfoService
import com.sample.crawler.earthquake.web.api.dataprovider.type.IDataProvider
import com.sample.crawler.earthquake.web.api.repository.AFADAPICallRepository
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@Component("AFADDataProvider")
class AFADDataProviderImpl(private val earthquakeInfoService: IEarthquakeInfoService) : LoggableClass(), IDataProvider {
    private val API_BASE_URL : String = "https://deprem.afad.gov.tr"
    private val PROVIDER_NAME = "AFAD API Provider"

    private val magnitude : Int = 0
    private val isUTCTime : Boolean = false
    private var lastDay : Int = 0
    private val apiCallEndpoint: AFADAPICallRepository

    init {
        val retrofitClient = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        apiCallEndpoint = retrofitClient.create(AFADAPICallRepository::class.java)
    }

    override fun providerToleranceParameters(): ToleranceParameters {
        val toleranceParameter = ToleranceParameters()
        toleranceParameter.reTryCount = 3
        toleranceParameter.waitUntilReTry = 30 * 1000
        return toleranceParameter
    }

    override fun initializeCallParameters() {
        val lastData = earthquakeInfoService.getLastDataForProvider(this.PROVIDER_NAME)
        val lastDataCreatedDate = lastData?.earthquakeTime ?: Date(-1)
        val now = Calendar.getInstance().time
        val dayDifferenceAfterLastRecord = CommonUtils.calculateDayDifferenceInDays(now, lastDataCreatedDate)

        lastDay = 50
        if (dayDifferenceAfterLastRecord < lastDay) {
            lastDay = if (dayDifferenceAfterLastRecord > 0L) dayDifferenceAfterLastRecord.toInt() else 1
        }
    }

    override fun makeCall(): Boolean {
        val utcTime = if (isUTCTime) 1 else 0
        val dataProviderCall = apiCallEndpoint.fetchLatestEarthquakeData(magnitude, utcTime, lastDay)

        try {
            val serviceResponse = dataProviderCall.execute()
            if (serviceResponse?.isSuccessful != true) {
                this.logger.warn("makeCall, service call process is failed, Detail: {}", serviceResponse?.errorBody())
                return false
            }

            val convertedData = this.convertResponseToEarthquakeInfo(serviceResponse.body())
            this.earthquakeInfoService.upsertDocuments(this.PROVIDER_NAME, convertedData)

            return true
        } catch (e: Exception) {
            this.logger.error("makeCall, call could not be executed, Detail", e)
            return false
        }
    }

    private fun convertResponseToEarthquakeInfo(serviceResponse: List<AFADLatestEarthquakeResponse>?) : List<EarthquakeInfo> {
        if (serviceResponse?.size ?: 0 > 0) {
            return serviceResponse!!.map { this.convertResponseToEarthquakeInfo(it) }
        }

        return Collections.emptyList()
    }

    private fun convertResponseToEarthquakeInfo(response: AFADLatestEarthquakeResponse) : EarthquakeInfo {
        val earthquakeInfo = EarthquakeInfo()
        earthquakeInfo.earthquakeTime = CommonUtils.safeConvertDatetime(response.time, Constants.DAY_FIRST_DATE_FORMAT)
        earthquakeInfo.dataProvider = this.PROVIDER_NAME
        earthquakeInfo.depthInfo = response.depth
        earthquakeInfo.intensityOfEarthquake = response.m

        val earthquakeLocation = Location()
        earthquakeLocation.latitude = response.lat
        earthquakeLocation.longitude = response.lon
        earthquakeLocation.locationDescription = this.buildLocationDescriptionString(response)

        return earthquakeInfo
    }

    private fun buildLocationDescriptionString(response: AFADLatestEarthquakeResponse) : String {
        if (this.isResponseStringEmpty(response.other)) {
            val descriptionBuilder = StringBuilder()
            if (!this.isResponseStringEmpty(response.town)) {
                descriptionBuilder
                        .append("Town: ")
                        .append(response.town)
                        .append(", ")
            }
            if (!this.isResponseStringEmpty(response.district)) {
                descriptionBuilder
                        .append("District: ")
                        .append(response.district)
                        .append(", ")
            }
            if (!this.isResponseStringEmpty(response.city)) {
                descriptionBuilder
                        .append("City: ")
                        .append(response.city)
                        .append(", ")
            }

            descriptionBuilder.append("Country: ").append(response.country)
            return descriptionBuilder.toString()
        }

        return response.other!!
    }

    private fun isResponseStringEmpty(responseString: String?) : Boolean {
        return responseString.isNullOrEmpty() || responseString.equals("-")
    }
}