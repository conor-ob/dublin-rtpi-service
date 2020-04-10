package io.rtpi.api

interface ServiceLocation {
    val id: String
    val name: String
    val service: Service
    val coordinate: Coordinate
    val properties: Map<String, Any>
}

data class StopLocation(
    override val id: String,
    override val name: String,
    override val service: Service,
    override val coordinate: Coordinate,
    override val properties: Map<String, Any>,
    val routes: List<Route>
) : ServiceLocation

data class DockLocation(
    override val id: String,
    override val name: String,
    override val service: Service,
    override val coordinate: Coordinate,
    override val properties: Map<String, Any>,
    val availableBikes: Int,
    val availableDocks: Int,
    val totalDocks: Int
) : ServiceLocation

data class Coordinate(
    val latitude: Double,
    val longitude: Double
)

data class Route(
    val id: String,
    val operator: Operator
)
