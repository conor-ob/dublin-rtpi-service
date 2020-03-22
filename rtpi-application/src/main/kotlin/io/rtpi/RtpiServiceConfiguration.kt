package io.rtpi

import io.dropwizard.Configuration
import java.time.Duration

class RtpiServiceConfiguration : Configuration() {
    lateinit var apiConfiguration: ApiConfiguration
    lateinit var cacheConfiguration: CacheConfiguration
}

data class ApiConfiguration(
    val aircoachBaseUrl: String? = null,
    val dublinBusBaseUrl: String? = null,
    val irishRailBaseUrl: String? = null,
    val jcDecauxBaseUrl: String? = null,
    val rtpiBaseUrl: String? = null
)

data class CacheConfiguration(
    val serviceLocationExpiry: Duration? = null,
    val liveDataExpiry: Duration? = null
)
