package io.rtpi.test.client

import com.google.gson.Gson
import io.reactivex.Single
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation
import io.rtpi.service.aircoach.AircoachStopService
import io.rtpi.service.buseireann.BusEireannStopService
import io.rtpi.service.dublinbikes.DublinBikesDockService
import io.rtpi.service.dublinbus.DublinBusStopService
import io.rtpi.service.irishrail.IrishRailStationService
import io.rtpi.service.luas.LuasStopService

class RtpiStaticDataClient {

    private val gson = Gson()

    private val aircoachWebScraper = AircoachStaticDataApi(gson)

    private val staticDataApi = NoOpStaticDataApi()

    private val dublinBusApi = DublinBusStaticDataApi()

    private val irishRailApi = IrishRailStaticDataApi()

    private val jcDecauxApi = JcDecauxStaticDataApi(gson)

    private val rtpiApi = RtpiStaticDataApi(gson)

    private val aircoachStaticDataClient = AircoachStaticDataClient(
        AircoachStopService(aircoachWebScraper, staticDataApi)
    )

    private val busEireannStaticDataClient = BusEireannStaticDataClient(
        BusEireannStopService(rtpiApi, rtpiApi)
    )

    private val dublinBikesStaticDataClient = DublinBikesStaticDataClient(
        DublinBikesDockService(jcDecauxApi)
    )

    private val dublinBusStaticDataClient = DublinBusStaticDataClient(
        DublinBusStopService(dublinBusApi, rtpiApi, rtpiApi)
    )

    private val irishRailStaticDataCLient = IrishRailStaticDataClient(
        IrishRailStationService(irishRailApi)
    )

    private val luasStaticDataClient = LuasStaticDataClient(
        LuasStopService(rtpiApi, rtpiApi)
    )

    fun getServiceLocations(service: Service): Single<List<ServiceLocation>> {
        return when (service) {
            Service.AIRCOACH -> aircoachStaticDataClient.getStops()
            Service.BUS_EIREANN -> busEireannStaticDataClient.getStops()
            Service.DUBLIN_BIKES -> dublinBikesStaticDataClient.getDocks()
            Service.DUBLIN_BUS -> dublinBusStaticDataClient.getStops()
            Service.IRISH_RAIL -> irishRailStaticDataCLient.getStations()
            Service.LUAS -> luasStaticDataClient.getStops()
            else -> TODO()
        }
    }
}
