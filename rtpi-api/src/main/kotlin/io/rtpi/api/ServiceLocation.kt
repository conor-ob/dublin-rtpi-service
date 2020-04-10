package io.rtpi.api

interface ServiceLocation {
    val id: String
    val name: String
    val service: Service
    val coordinate: Coordinate
    val properties: MutableMap<String, Any>
}

data class StopLocation(
    override val id: String,
    override val name: String,
    override val service: Service,
    override val coordinate: Coordinate,
    val routes: List<RouteGroup>,
    override val properties: MutableMap<String, Any> = mutableMapOf()
) : ServiceLocation

data class DockLocation(
    override val id: String,
    override val name: String,
    override val service: Service,
    override val coordinate: Coordinate,
    val availableBikes: Int,
    val availableDocks: Int,
    val totalDocks: Int,
    override val properties: MutableMap<String, Any> = mutableMapOf()
) : ServiceLocation

data class Coordinate(
    val latitude: Double,
    val longitude: Double
)

data class RouteGroup(
    val operator: Operator,
    val routes: List<String>
)
