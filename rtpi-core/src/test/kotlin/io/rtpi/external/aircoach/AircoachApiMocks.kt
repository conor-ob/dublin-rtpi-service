package io.rtpi.external.aircoach

fun createAircoachStopJson(
    id: String = "",
    stopId: String = "",
    name: String = "",
    shortName: String = "",
    linkName: String = "",
    ticketName: String = "",
    place: String = "",
    stopLatitude: Double = 0.0,
    stopLongitude: Double = 0.0,
    services: List<AircoachStopServiceJson> = emptyList()
): AircoachStopJson {
    return AircoachStopJson(
        id = id,
        stopId = stopId,
        name = name,
        shortName = shortName,
        linkName = linkName,
        ticketName = ticketName,
        place = place,
        stopLatitude = stopLatitude,
        stopLongitude = stopLongitude,
        services = services
    )
}
