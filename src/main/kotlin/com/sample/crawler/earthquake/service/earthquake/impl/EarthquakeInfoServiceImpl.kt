package com.sample.crawler.earthquake.service.earthquake.impl

import com.sample.crawler.earthquake.base.LoggableClass
import com.sample.crawler.earthquake.data.entity.EarthquakeInfoDocument
import com.sample.crawler.earthquake.data.entity.base.IdentityDocument
import com.sample.crawler.earthquake.data.repository.EarthquakeInfoRepository
import com.sample.crawler.earthquake.dto.EarthquakeInfo
import com.sample.crawler.earthquake.service.earthquake.type.IEarthquakeInfoService
import org.dozer.DozerBeanMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EarthquakeInfoServiceImpl(private val earthquakeInfoRepository: EarthquakeInfoRepository,
                                private val mapper: DozerBeanMapper) : LoggableClass(), IEarthquakeInfoService {
    @Transactional
    override fun update(bulkData: List<EarthquakeInfo>) {
        val convertedDataList = this.convertListToDocument(bulkData)
        this.earthquakeInfoRepository.saveAll(convertedDataList)
    }

    @Transactional
    override fun insert(bulkData: List<EarthquakeInfo>) {
        val convertedDataList = this.convertListToDocument(bulkData)
        for (earthquakeInfoDocument in convertedDataList) {
            this.earthquakeInfoRepository.insert(earthquakeInfoDocument)
        }
    }

    private fun convertListToDocument(bulkData: List<EarthquakeInfo>) : List<EarthquakeInfoDocument> {
        val convertedDataList = mutableListOf<EarthquakeInfoDocument>()
        for (data in bulkData) {
            val document = mapper.map(data, EarthquakeInfoDocument::class.java)
            convertedDataList.add(document)
        }

        return convertedDataList
    }

    @Transactional(readOnly = true)
    override fun getLastInsertedData(): EarthquakeInfo? {
        val lastInsertedRow = earthquakeInfoRepository.findAll(IdentityDocument.orderByDesc())
                .firstOrNull()
        if (lastInsertedRow != null) {
            return this.mapper.map(lastInsertedRow, EarthquakeInfo::class.java)
        }

        return null
    }

    @Transactional(readOnly = true)
    override fun getLastUpdatedData(): EarthquakeInfo? {
        val lastUpdatedRow = earthquakeInfoRepository.findAllByUpdatedTimeIsNotNullOrderByUpdatedTimeDesc()
                .firstOrNull()
        if (lastUpdatedRow != null) {
            return this.mapper.map(lastUpdatedRow, EarthquakeInfo::class.java)
        }

        return null
    }

    @Transactional
    override fun upsertInfoToDB(info: EarthquakeInfo): Boolean {
        val documentId = info.earthquakeTime?.time ?: -1
        try {
            val document = mapper.map(info, EarthquakeInfoDocument::class.java)
            document.id = documentId

            val isDocumentExists = earthquakeInfoRepository.existsById(documentId)
            if (isDocumentExists) {
                earthquakeInfoRepository.save(document)
            } else {
                earthquakeInfoRepository.insert(document)
            }

            return true
        } catch (e : Exception) {
            this.logger.error("upsertInfoToDB, document could not be saved to DB, id: {}, Detail: ", documentId, e)
            return false
        }
    }
}