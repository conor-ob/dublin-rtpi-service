package io.rtpi.service.aircoach

import io.rtpi.external.aircoach.AbstractAircoachWebScraper
import io.rtpi.external.aircoach.AircoachStopJson
import io.rtpi.external.aircoach.AircoachStopServiceJson
import jdk.nashorn.api.scripting.ScriptObjectMirror
import javax.script.ScriptEngineManager

class JsoupAircoachWebScraper(private val aircoachBaseUrl: String) : AbstractAircoachWebScraper(aircoachBaseUrl) {

    override fun scrapeStops(javascript: String): List<AircoachStopJson> {
        val stops = mutableListOf<AircoachStopJson>()
        val factory = ScriptEngineManager()
        val engine = factory.getEngineByName("javascript")
        engine.eval(javascript)
        val stopArray = engine.get(stopArray) as ScriptObjectMirror
        for (stopObject in stopArray.values) {
            val stop = stopObject as ScriptObjectMirror
            val id = stop["id"] as String
            val stopId = stop["stopId"] as String
            val name = stop["name"] as String
            val shortName = stop["shortName"] as String
            val linkName = stop["linkName"] as String
            val ticketName = stop["ticketName"] as String
            val place = stop["place"] as String
            val latitude = stop["stopLatitude"] as Double
            val longitude = stop["stopLongitude"] as Double
            val services = stop["services"] as ScriptObjectMirror
            val servicesJson = mutableListOf<AircoachStopServiceJson>()
            for (serviceObject in services.values) {
                val service = serviceObject as ScriptObjectMirror
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
