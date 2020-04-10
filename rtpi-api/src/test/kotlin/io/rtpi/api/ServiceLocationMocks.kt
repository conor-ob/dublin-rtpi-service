package io.rtpi.api

fun createAircoachStop(
    id: String = "",
    name: String = "",
    coordinate: Coordinate = Coordinate(0.0, 0.0),
    operators: Set<Operator> = emptySet(),
    routes: List<RouteGroup> = listOf(RouteGroup(operator = Operator.AIRCOACH, routes = emptyList()))
): ServiceLocation {
    return StopLocation(
        id = id,
        name = name,
        coordinate = coordinate,
        routes = routes,
        service = Service.AIRCOACH,
        properties = mutableMapOf()
    )
}

fun createDublinBusStop(
    id: String = "",
    name: String = "",
    coordinate: Coordinate = Coordinate(0.0, 0.0),
    operators: Set<Operator> = emptySet(),
    routes: List<RouteGroup> = emptyList()
): ServiceLocation {
    return StopLocation(
        id = id,
        name = name,
        coordinate = coordinate,
        routes = routes,
        service = Service.DUBLIN_BUS,
        properties = mutableMapOf()
    )
}

fun createLuasStop(
    id: String = "",
    name: String = "",
    coordinate: Coordinate = Coordinate(0.0, 0.0),
    operators: Set<Operator> = emptySet(),
    routes: List<RouteGroup> = emptyList()
) = StopLocation(
    id = id,
    name = name,
    coordinate = coordinate,
    routes = routes,
    service = Service.LUAS,
    properties = mutableMapOf()
)
