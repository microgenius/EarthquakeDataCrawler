package com.sample.crawler.earthquake

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class EarthquakeDataCrawlerApplication

fun main(args: Array<String>) {
    runApplication<EarthquakeDataCrawlerApplication>(*args)
}