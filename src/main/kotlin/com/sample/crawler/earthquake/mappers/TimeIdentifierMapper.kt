package com.sample.crawler.earthquake.mappers

import org.dozer.CustomConverter
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId

class TimeIdentifierMapper : CustomConverter {
    override fun convert(destination: Any?, source: Any?, destinationClass: Class<*>?, sourceClass: Class<*>?): Any? {
        if (source is Date && destination is Long) {
            return source.time
        }

        return null
    }
}