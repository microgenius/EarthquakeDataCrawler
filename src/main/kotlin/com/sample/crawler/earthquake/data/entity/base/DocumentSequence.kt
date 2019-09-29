package com.sample.crawler.earthquake.data.entity.base

import org.springframework.data.mongodb.core.mapping.Document

/**
 * @author tanriverdi
 */
@Document
class DocumentSequence : IdentityDocument<String>() {
    var sequence: Long = 0
}
