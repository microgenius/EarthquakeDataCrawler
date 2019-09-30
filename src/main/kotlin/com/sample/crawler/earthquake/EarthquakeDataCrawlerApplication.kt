package com.sample.crawler.earthquake

import org.dozer.DozerBeanMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class EarthquakeDataCrawlerApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<EarthquakeDataCrawlerApplication>(*args)
        }
    }

    @Bean
    fun getDozerMapperSourceList(): List<ClassPathResource> {
        return mutableListOf(ClassPathResource("/dozer/mapping.xml"))
    }

    @Bean
    fun getMapper(dozerMapperSourceList: List<ClassPathResource>): DozerBeanMapper {
        return DozerBeanMapper(
                dozerMapperSourceList
                        .map { it.uri }
                        .map { it.toString() }
        )
    }
}