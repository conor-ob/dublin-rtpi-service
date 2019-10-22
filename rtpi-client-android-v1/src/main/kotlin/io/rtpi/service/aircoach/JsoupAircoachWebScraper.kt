package io.rtpi.service.aircoach

import io.rtpi.resource.aircoach.AbstractAircoachWebScraper
import io.rtpi.resource.aircoach.AircoachStopJson
import io.rtpi.resource.aircoach.AircoachStopServiceJson
import org.mozilla.javascript.NativeArray
import org.mozilla.javascript.NativeObject
import javax.script.ScriptEngineManager

class JsoupAircoachWebScraper(private val aircoachBaseUrl: String) : AbstractAircoachWebScraper(aircoachBaseUrl) {

    override fun scrapeStops(javascript: String): List<AircoachStopJson> {
        val stops = mutableListOf<AircoachStopJson>()
        val factory = ScriptEngineManager()
        val engine = factory.getEngineByName("rhino")
        engine.eval(javascript)
        val stopArray = engine.get(stopArray) as NativeArray
        for (stopObject in stopArray) {
            val stop = stopObject as NativeObject
            val id = stop["id"] as String
            val stopId = stop["stopId"] as String
            val name = stop["name"] as String
            val shortName = stop["shortName"] as String
            val linkName = stop["linkName"] as String
            val ticketName = stop["ticketName"] as String
            val place = stop["place"] as String
            val latitude = stop["stopLatitude"] as Double
            val longitude = stop["stopLongitude"] as Double
            val services = stop["services"] as NativeArray
            val servicesJson = mutableListOf<AircoachStopServiceJson>()
            for (serviceObject in services) {
                val service = serviceObject as NativeObject
                val route = service["route"] as String
                val dir = service["dir"] as String
                val serviceLinkName = service["linkName"] as String
                servicesJson.add(
                    AircoachStopServiceJson(
                        route,
                        dir,
                        serviceLinkName
                    )
                )
            }
            stops.add(
                AircoachStopJson(
                    id,
                    stopId,
                    name,
                    shortName,
                    linkName,
                    ticketName,
                    place,
                    latitude,
                    longitude,
                    servicesJson
                )
            )
        }
        return stops
    }

}
