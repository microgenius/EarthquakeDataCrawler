package com.sample.crawler.earthquake.base

import java.text.SimpleDateFormat
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.math.abs

class CommonUtils : LoggableClass() {
    companion object {
        fun safeConvertDatetime(value: String?, format: String = Constants.YEAR_FIRST_DATE_FORMAT) : Date? {
            return try {
                val simpleDateFormat = SimpleDateFormat(format)
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

        fun calculateDayDifferenceInDays(date: Date, otherDate: Date) : Long {
            val differenceAsTimestamp = abs(date.time - otherDate.time)
            return differenceAsTimestamp / (24 * 60 * 60 * 1000)
        }
    }
}