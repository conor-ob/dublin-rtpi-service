package io.rtpi.api

fun createAircoachStop(
    id: String = "",
    name: String = "",
    coordinate: Coordinate = Coordinate(0.0, 0.0),
    operators: Set<Operator> = emptySet(),
    routes: List<Route> = emptyList()
): AircoachStop {
    return AircoachStop(
        id = id,
        name = name,
        coordinate = coordinate,
        operators = operators,
        routes = routes
    )
}

fun createDublinBusStop(
    id: String = "",
    name: String = "",
    coordinate: Coordinate = Coordinate(0.0, 0.0),
    operators: Set<Operator> = emptySet(),
    routes: List<Route> = emptyList()
): DublinBusStop {
    return DublinBusStop(
        id = id,
        name = name,
        coordinate = coordinate,
        operators = operators,
        routes = routes
    )
}
