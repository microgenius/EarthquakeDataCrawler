package com.sample.crawler.earthquake.base

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern


class Constants {
    companion object {
        @JvmStatic
        val DEF_DATE_FORMAT: DateTimeFormatter = ofPattern("yyyy.MM.dd' 'HH:mm:ss")
    }
}