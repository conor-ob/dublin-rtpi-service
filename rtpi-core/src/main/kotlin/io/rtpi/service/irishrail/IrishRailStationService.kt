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
            .map { xml ->
                IrishRailStation(
                    id = xml.code!!,
                    name = xml.name!!,
                    coordinate = Coordinate(xml.latitude!!, xml.longitude!!),
                    operators = setOf(Operator.DART, Operator.COMMUTER, Operator.INTERCITY) //TODO
                )
            }
    }

}
