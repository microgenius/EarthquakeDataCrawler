package com.sample.crawler.earthquake.data.entity.base

import com.sample.crawler.earthquake.data.entity.EarthquakeInfoDocument
import com.sample.crawler.earthquake.data.entity.sequence.DocumentSequenceGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component

/**
 * @author tanriverdi
 */
@Component
class EarthquakeInfoDocumentListener : AbstractMongoEventListener<EarthquakeInfoDocument>() {
    @Autowired
    private val documentSequenceGenerator: DocumentSequenceGenerator? = null

    override fun onBeforeConvert(event: BeforeConvertEvent<EarthquakeInfoDocument>) {
        if (event.source.id ?: -1 <= 0) {
            val sequenceName = event.collectionName!! + "_seq"
            event.source.id = documentSequenceGenerator!!.generateSequence(sequenceName)
        }

        super.onBeforeConvert(event)
    }
}
