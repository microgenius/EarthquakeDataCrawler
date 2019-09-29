package com.sample.crawler.earthquake.data.entity.base

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.mapping.Document

/**
 * @author tanriverdi
 */
@Document
open class IdentityDocument<IdType> {
    @Id
    var id: IdType? = null

    companion object {
        fun orderByDesc() : Sort {
            return Sort(Sort.Direction.DESC, "id")
        }
    }
}
