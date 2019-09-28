package ie.dublin.rtpi.service.irishrail

import ie.dublin.rtpi.api.Coordinate
import ie.dublin.rtpi.api.IrishRailStation
import ie.dublin.rtpi.api.Operator
import ie.dublin.rtpi.ktx.validate
import ie.dublin.rtpi.resource.irishrail.IrishRailApi

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
