package io.rtpi.api

fun createAircoachStop(
    id: String = "",
    name: String = "",
    coordinate: Coordinate = Coordinate(0.0, 0.0),
    operators: Set<Operator> = setOf(Operator.AIRCOACH),
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
