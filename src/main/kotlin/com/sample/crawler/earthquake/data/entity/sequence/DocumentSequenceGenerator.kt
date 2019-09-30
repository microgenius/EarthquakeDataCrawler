package com.sample.crawler.earthquake.data.entity.sequence

import com.sample.crawler.earthquake.data.entity.base.DocumentSequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

/**
 * @author tanriverdi
 */
@Service
class DocumentSequenceGenerator {

    @Autowired
    private val mongoOperations: MongoOperations? = null

    fun generateSequence(seqName: String): Long {
        val sequenceDocument = this.mongoOperations!!.findAndModify(
                query(where("_id").`is`(seqName)),
                Update().inc("sequence", 1),
                options().returnNew(true).upsert(true),
                DocumentSequence::class.java
        )

        return sequenceDocument?.sequence ?: 1
    }
}
