package com.sample.crawler.earthquake.service.earthquake.impl

import com.sample.crawler.earthquake.base.LoggableClass
import com.sample.crawler.earthquake.data.entity.EarthquakeInfoDocument
import com.sample.crawler.earthquake.data.repository.EarthquakeInfoRepository
import com.sample.crawler.earthquake.dto.EarthquakeInfo
import com.sample.crawler.earthquake.service.earthquake.type.IEarthquakeInfoService
import org.dozer.DozerBeanMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class EarthquakeInfoServiceImpl(private val earthquakeInfoRepository: EarthquakeInfoRepository,
                                private val mapper: DozerBeanMapper) :
        LoggableClass(), IEarthquakeInfoService {

    @Transactional(readOnly = true)
    override fun getLastDataForProvider(providerName: String): EarthquakeInfo? {
        val latestDataFromProvider = earthquakeInfoRepository.findTop1ByDataProviderOrderByEarthquakeTimeDesc(providerName)
        if (latestDataFromProvider != null) {
            return this.mapper.map(latestDataFromProvider, EarthquakeInfo::class.java)
        }

        return null
    }

    @Transactional
    override fun update(bulkData: List<EarthquakeInfo>) {
        if (bulkData.isNotEmpty()) {
            val convertedDataList = this.convertListToDocument(bulkData)
            this.earthquakeInfoRepository.saveAll(convertedDataList)
            this.logger.info("update, {} Document(s) has updated inside [EarthquakeInfo]", bulkData.size)
        }
    }

    @Transactional
    override fun insert(bulkData: List<EarthquakeInfo>) {
        if (bulkData.isNotEmpty()) {
            val convertedDataList = this.convertListToDocument(bulkData)
            this.earthquakeInfoRepository.insert(convertedDataList)
            this.logger.info("insert, {} Document(s) has inserted to [EarthquakeInfo]", bulkData.size)
        }
    }

    private fun convertListToDocument(bulkData: List<EarthquakeInfo>): List<EarthquakeInfoDocument> {
        val convertedDataList = mutableListOf<EarthquakeInfoDocument>()
        for (data in bulkData) {
            val document = mapper.map(data, EarthquakeInfoDocument::class.java)
            convertedDataList.add(document)
        }

        return convertedDataList
    }

    @Transactional(readOnly = true)
    override fun getLastUpdatedData(providerName: String): EarthquakeInfo? {
        val lastUpdatedRow = earthquakeInfoRepository.findAllByDataProviderAndUpdatedTimeIsNotNullOrderByUpdatedTimeDesc(providerName)
                .firstOrNull()
        if (lastUpdatedRow != null) {
            return this.mapper.map(lastUpdatedRow, EarthquakeInfo::class.java)
        }

        return null
    }

    @Transactional
    override fun upsertDocuments(providerName: String, convertedData: List<EarthquakeInfo>) {
        val lastCreatedTime = this.getLastDataForProvider(providerName)
                ?.earthquakeTime ?: Date(-1)

        val lastUpdatedTime = this.getLastUpdatedData(providerName)
                ?.earthquakeTime ?: Date(-1)

        val nonExistsDocuments = convertedData
                .filter { it.earthquakeTime!!.after(lastCreatedTime) }

        val existsDocuments = convertedData
                .filter { it.earthquakeTime!!.after(lastUpdatedTime) }
                .filter { Objects.nonNull(it.updatedTime) }
                .filter { self -> nonExistsDocuments.none { other -> self.earthquakeTime!!.equals(other.earthquakeTime) } }

        this.insert(nonExistsDocuments)
        this.update(existsDocuments)
    }
}