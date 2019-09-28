package com.sample.crawler.earthquake.base


import org.slf4j.LoggerFactory

/**
 * @author tanriverdi
 */
open class LoggableClass {
    protected val logger = LoggerFactory.getLogger(this.javaClass)
}
