package com.sample.crawler.earthquake.base

import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class CommonUtils : LoggableClass() {
    companion object {
        fun safeConvertDatetime(value: String?) : LocalDateTime? {
            return try {
                LocalDateTime.parse(value, Constants.DEF_DATE_FORMAT)
            } catch (ex: DateTimeParseException) {
                ex.printStackTrace()
                null
            }
        }
    }
}