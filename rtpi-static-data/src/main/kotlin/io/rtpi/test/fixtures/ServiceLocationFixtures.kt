package io.rtpi.test.fixtures

import io.rtpi.api.Coordinate
import io.rtpi.api.DockLocation
import io.rtpi.api.RouteGroup
import io.rtpi.api.Service
import io.rtpi.api.StopLocation
import java.util.UUID
import kotlin.random.Random

fun createStopLocation(
    id: String = UUID.randomUUID().toString(),
    name: String = UUID.randomUUID().toString(),
    service: Service = Service.values().random(),
    coordinate: Coordinate = Coordinate(latitude = randomCoordinatePoint(), longitude = randomCoordinatePoint()),
    routeGroups: List<RouteGroup> = service.operators.map { operator ->
        RouteGroup(
            operator = operator,
            routes = listOf(UUID.randomUUID().toString())
        )
    }
) = StopLocation(id, name, service, coordinate, routeGroups)

fun createDockLocation(
    id: String = UUID.randomUUID().toString(),
    name: String = UUID.randomUUID().toString(),
    service: Service = Service.values().random(),
    coordinate: Coordinate = Coordinate(latitude = randomCoordinatePoint(), longitude = randomCoordinatePoint()),
    availableBikes: Int = randomInt(),
    availableDocks: Int = randomInt(),
    totalDocks: Int = randomInt()
) = DockLocation(id, name, service, coordinate, availableBikes, availableDocks, totalDocks)

fun randomCoordinatePoint(): Double = Random.nextDouble(from = 0.0, until = 90.0)

fun randomInt(): Int = Random.nextInt(from = 0, until = 100)
