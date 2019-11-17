package io.rtpi.service.irishrail

import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.IrishRailStation
import io.rtpi.api.Operator
import io.rtpi.resource.irishrail.IrishRailApi

class IrishRailStationService(private val irishRailApi: IrishRailApi) {

    fun getStations(): Single<List<IrishRailStation>> {
        return irishRailApi.getAllStationsXml()
            .map {
                it.stations.filter { xml ->
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
    }

    //TODO
    private fun mapOperators(stationId: String): Set<Operator> {
        return when (stationId) {
            "BRAY",
            "CNLLY",
            "DLERY",
            "GSTNS",
            "MHIDE",
            "PERSE",
            "TARA" -> setOf(Operator.COMMUTER, Operator.DART, Operator.INTERCITY)
            "BROCK",
            "GCDK",
            "LDWNE" -> setOf(Operator.COMMUTER, Operator.DART)
            "HZLCH",
            "HAZEF" -> setOf(Operator.COMMUTER)
            "BTSTN",
            "SEAPT",
            "SMONT" -> setOf(Operator.DART)
            else -> setOf(Operator.INTERCITY)
        }
    }

}
