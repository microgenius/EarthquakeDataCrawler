package com.sample.crawler.earthquake.mappers

import com.sample.crawler.earthquake.base.CommonUtils
import org.dozer.CustomConverter
import java.sql.Date

class StringDateConverter : CustomConverter {
    override fun convert(destination: Any?, source: Any?, destinationClass: Class<*>?, sourceClass: Class<*>?): Any? {
        if (source is String && destination is Date) {
            return CommonUtils.safeConvertDatetime(source)
        }

        return null
    }
}