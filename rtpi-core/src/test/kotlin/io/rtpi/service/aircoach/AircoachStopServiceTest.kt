package io.rtpi.service.aircoach

import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.RouteGroup
import io.rtpi.api.Service
import io.rtpi.external.aircoach.AircoachStopJson
import io.rtpi.external.aircoach.AircoachWebScraper
import io.rtpi.external.aircoach.createAircoachStopJson
import io.rtpi.external.staticdata.StaticDataApi
import io.rtpi.test.fixtures.createStopLocation
import java.net.UnknownHostException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AircoachStopServiceTest {

    @Test
    fun `get from static data api if api throws error`() {
        // arrange
        val aircoachWebScraper = object : AircoachWebScraper {
            override fun scrapeStops(): List<AircoachStopJson> {
                throw UnknownHostException()
            }
        }
        val staticDatApi = object : StaticDataApi {
            override fun getAircoachStops(): Single<List<AircoachStopJson>> {
                return Single.just(
                    listOf(
                        createAircoachStopJson(
                            id = "2",
                            name = "Airport2",
                            stopLatitude = 53.5453,
                            stopLongitude = -6.4311
                        )
                    )
                )
            }
            override fun getBusEireannStops() = throw NotImplementedError()
            override fun getDublinBikesDocks() = throw NotImplementedError()
            override fun getDublinBusStops() = throw NotImplementedError()
            override fun getGoAheadStops() = throw NotImplementedError()
            override fun getIrishRailStations() = throw NotImplementedError()
            override fun getLuasStops() = throw NotImplementedError()
        }
        val aircoachStopService = AircoachStopService(aircoachWebScraper, staticDatApi)

        // act
        val observer = aircoachStopService.getStops().test()

        // assert
        observer.assertValue(
            listOf(
                createStopLocation(
                    id = "2",
                    name = "Airport2",
                    service = Service.AIRCOACH,
                    coordinate = Coordinate(latitude = 53.5453, longitude = -6.4311),
                    routeGroups = listOf(RouteGroup(operator = Operator.AIRCOACH, routes = emptyList()))
                )
            )
        )
        observer.dispose()
    }

    @Test
    fun `get from static data api if api returns empty`() {
        // arrange
        val aircoachWebScraper = object : AircoachWebScraper {
            override fun scrapeStops(): List<AircoachStopJson> {
                return emptyList()
            }
        }
        val staticDatApi = object : StaticDataApi {
            override fun getAircoachStops(): Single<List<AircoachStopJson>> {
                return Single.just(
                    listOf(
                        createAircoachStopJson(
                            id = "1",
                            name = "Airport1",
                            stopLatitude = 53.5453,
                            stopLongitude = -6.4311
                        )
                    )
                )
            }
            override fun getBusEireannStops() = throw NotImplementedError()
            override fun getDublinBikesDocks() = throw NotImplementedError()
            override fun getDublinBusStops() = throw NotImplementedError()
            override fun getGoAheadStops() = throw NotImplementedError()
            override fun getIrishRailStations() = throw NotImplementedError()
            override fun getLuasStops() = throw NotImplementedError()
        }
        val aircoachStopService = AircoachStopService(aircoachWebScraper, staticDatApi)

        // act
        val observer = aircoachStopService.getStops().test()

        // assert
        observer.assertValue(
            listOf(
                createStopLocation(
                    id = "1",
                    name = "Airport1",
                    service = Service.AIRCOACH,
                    coordinate = Coordinate(latitude = 53.5453, longitude = -6.4311),
                    routeGroups = listOf(RouteGroup(operator = Operator.AIRCOACH, routes = emptyList()))
                )
            )
        )
        observer.dispose()
    }

    @Test
    fun `do not get from static data api if api returns ok`() {
        // arrange
        val aircoachWebScraper = object : AircoachWebScraper {
            override fun scrapeStops(): List<AircoachStopJson> {
                return listOf(
                    createAircoachStopJson(
                        id = "1",
                        name = "Airport1",
                        stopLatitude = 53.5453,
                        stopLongitude = -6.4311
                    ),
                    createAircoachStopJson(
                        id = "2",
                        name = "Airport2",
                        stopLatitude = 53.5453,
                        stopLongitude = -6.4311
                    )
                )
            }
        }
        val staticDatApi = object : StaticDataApi {
            override fun getAircoachStops(): Single<List<AircoachStopJson>> {
                return Single.just(
                    listOf(
                        createAircoachStopJson(
                            id = "3",
                            name = "Airport3",
                            stopLatitude = 53.5453,
                            stopLongitude = -6.4311
                        )
                    )
                )
            }
            override fun getBusEireannStops() = throw NotImplementedError()
            override fun getDublinBikesDocks() = throw NotImplementedError()
            override fun getDublinBusStops() = throw NotImplementedError()
            override fun getGoAheadStops() = throw NotImplementedError()
            override fun getIrishRailStations() = throw NotImplementedError()
            override fun getLuasStops() = throw NotImplementedError()
        }
        val aircoachStopService = AircoachStopService(aircoachWebScraper, staticDatApi)

        // act
        val aircoachStops = aircoachStopService.getStops().blockingGet()

        // assert
        assertThat(aircoachStops).containsExactly(
            createStopLocation(
                id = "1",
                name = "Airport1",
                service = Service.AIRCOACH,
                coordinate = Coordinate(latitude = 53.5453, longitude = -6.4311),
                routeGroups = listOf(RouteGroup(operator = Operator.AIRCOACH, routes = emptyList()))
            ),
            createStopLocation(
                id = "2",
                name = "Airport2",
                service = Service.AIRCOACH,
                coordinate = Coordinate(latitude = 53.5453, longitude = -6.4311),
                routeGroups = listOf(RouteGroup(operator = Operator.AIRCOACH, routes = emptyList()))
            )
        )
    }
}
