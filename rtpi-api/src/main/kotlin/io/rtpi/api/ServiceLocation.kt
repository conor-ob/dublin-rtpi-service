package io.rtpi.api

interface ServiceLocation {

    val id: String

    val name: String

    val coordinate: Coordinate

    val service: Service

    val operators: Set<Operator>

    val data: MutableMap<String, Any>

}

data class Coordinate(
    val latitude: Double,
    val longitude: Double
)

data class AircoachStop(
    override val id: String,
    override val name: String,
    override val coordinate: Coordinate,
    override val operators: Set<Operator>,
    val routes: Map<Operator, List<String>>
) : ServiceLocation {

    override val service = Service.AIRCOACH

    override val data: MutableMap<String, Any> = mutableMapOf()

}

data class BusEireannStop(
    override val id: String,
    override val name: String,
    override val coordinate: Coordinate,
    override val operators: Set<Operator>,
    val routes: Map<Operator, List<String>>
) : ServiceLocation {

    override val service = Service.BUS_EIREANN

    override val data: MutableMap<String, Any> = mutableMapOf()

}

data class DublinBikesDock(
    override val id: String,
    override val name: String,
    override val coordinate: Coordinate,
    override val operators: Set<Operator>,
    val docks: Int,
    val availableBikes: Int,
    val availableDocks: Int
) : ServiceLocation {

    override val service = Service.DUBLIN_BIKES

    override val data: MutableMap<String, Any> = mutableMapOf()

}

data class DublinBusStop(
    override val id: String,
    override val name: String,
    override val coordinate: Coordinate,
    override val operators: Set<Operator>,
    val routes: Map<Operator, List<String>>
) : ServiceLocation {

    override val service = Service.DUBLIN_BUS

    override val data: MutableMap<String, Any> = mutableMapOf()

}

data class IrishRailStation(
    override val id: String,
    override val name: String,
    override val coordinate: Coordinate,
    override val operators: Set<Operator>
) : ServiceLocation {

    override val service = Service.IRISH_RAIL

    override val data: MutableMap<String, Any> = mutableMapOf()

}

data class LuasStop(
    override val id: String,
    override val name: String,
    override val coordinate: Coordinate,
    override val operators: Set<Operator>,
    val routes: Map<Operator, List<String>>
) : ServiceLocation {

    override val service = Service.LUAS

    override val data: MutableMap<String, Any> = mutableMapOf()

}
