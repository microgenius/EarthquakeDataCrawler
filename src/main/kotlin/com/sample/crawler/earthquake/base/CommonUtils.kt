package com.sample.crawler.earthquake.base

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class CommonUtils : LoggableClass() {
    companion object {
        fun safeConvertDatetime(value: String?) : Date? {
            return try {
                val simpleDateFormat = SimpleDateFormat(Constants.DEF_DATE_FORMAT)
                simpleDateFormat.parse(
                        value
                                ?.replace("(", "")
                                ?.replace(")", "")
                )
            } catch (ex: DateTimeParseException) {
                ex.printStackTrace()
                null
            }
        }
    }
}