package io.rtpi

import io.dropwizard.Configuration

class RtpiServiceConfiguration : Configuration() {

    lateinit var apiConfig: ApiConfig

}

data class ApiConfig(
    val aircoachBaseUrl: String? = null,
    val dublinBusBaseUrl: String? = null,
    val irishRailBaseUrl: String? = null,
    val jcDecauxBaseUrl: String? = null,
    val rtpiBaseUrl: String? = null
)
