package io.rtpi.service.irishrail

import io.rtpi.api.Coordinate
import io.rtpi.api.IrishRailStation
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.irishrail.IrishRailApi

class IrishRailStationService(private val irishRailApi: IrishRailApi) {

    fun getStations(): List<IrishRailStation> {
        return irishRailApi.getAllStationsXml()
            .validate()
            .stations
            .filter { xml ->
                xml.code != null
                    && xml.name != null
                    && xml.latitude != null
                    && xml.longitude != null
                    && xml.latitude != 0.0
                    && xml.longitude != 0.0
            }.map { xml ->
                IrishRailStation(
                    id = xml.code!!.trim().toUpperCase(),
                    name = xml.name!!.trim(),
                    coordinate = Coordinate(xml.latitude!!, xml.longitude!!),
                    operators = mapOperators(xml.code!!.trim().toUpperCase())
                )
            }
    }

    //TODO
    private fun mapOperators(stationId: String): Set<Operator> {
        return when (stationId) {
            "BRAY",
            "CNLLY",
            "DLERY",
            "GSTNS",
            "MHIDE",
            "PERSE" -> setOf(Operator.COMMUTER, Operator.DART, Operator.INTERCITY)
            "BROCK",
            "GCDK",
            "LDWNE" -> setOf(Operator.COMMUTER, Operator.DART)
            "HZLCH",
            "HAZEF" -> setOf(Operator.COMMUTER)
            "BTSTN",
            "SMONT" -> setOf(Operator.DART)
            else -> setOf(Operator.INTERCITY)
        }
    }

}
