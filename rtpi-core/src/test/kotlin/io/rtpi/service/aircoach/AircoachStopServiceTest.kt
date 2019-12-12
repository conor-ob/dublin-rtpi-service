package io.rtpi.service.aircoach

import io.reactivex.Single
import io.rtpi.api.Operator
import io.rtpi.api.createAircoachStop
import io.rtpi.external.aircoach.AircoachStopJson
import io.rtpi.external.aircoach.AircoachWebScraper
import io.rtpi.external.aircoach.createAircoachStopJson
import io.rtpi.external.staticdata.StaticDataApi
import org.junit.Test
import java.net.UnknownHostException

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
                            name = "Airport"
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
                createAircoachStop(
                    name = "Airport",
                    operators = setOf(Operator.AIRCOACH)
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
                            name = "Airport"
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
                createAircoachStop(
                    name = "Airport",
                    operators = setOf(Operator.AIRCOACH)
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
                        name = "Airport1"
                    ),
                    createAircoachStopJson(
                        name = "Airport2"
                    )
                )
            }
        }
        val staticDatApi = object : StaticDataApi {
            override fun getAircoachStops(): Single<List<AircoachStopJson>> {
                return Single.just(
                    listOf(
                        createAircoachStopJson(
                            name = "Airport3"
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
                createAircoachStop(
                    name = "Airport1",
                    operators = setOf(Operator.AIRCOACH)
                ),
                createAircoachStop(
                    name = "Airport2",
                    operators = setOf(Operator.AIRCOACH)
                )
            )
        )
        observer.dispose()
    }

}
