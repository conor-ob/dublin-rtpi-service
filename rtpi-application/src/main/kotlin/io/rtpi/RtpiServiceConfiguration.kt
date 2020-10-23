package io.rtpi

import io.dropwizard.Configuration

class RtpiServiceConfiguration : Configuration() {
    lateinit var apiConfiguration: ApiConfiguration
    lateinit var cacheConfiguration: CacheConfiguration
}

data class ApiConfiguration(
    val aircoachBaseUrl: String? = null,
    val dublinBusBaseUrl: String? = null,
    val irishRailBaseUrl: String? = null,
    val jcDecauxBaseUrl: String? = null,
    val rtpiBaseUrl: String? = null,
    val rtpiFallbackBaseUrl: String? = null
)

data class CacheConfiguration(
    val serviceLocationCacheSpec: String? = null,
    val liveDataCacheSpec: String? = null
)
