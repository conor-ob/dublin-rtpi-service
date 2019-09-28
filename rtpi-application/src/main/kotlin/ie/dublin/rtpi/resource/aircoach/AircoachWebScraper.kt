package ie.dublin.rtpi.resource.aircoach

import jdk.nashorn.api.scripting.ScriptObjectMirror
import org.jsoup.Jsoup
import javax.script.ScriptEngineManager

interface AircoachWebScraper {

    fun scrapeStops(): List<AircoachStopJson>

}

class JsoupAircoachWebScraper(private val aircoachBaseUrl: String) : AircoachWebScraper {

    private val path = "stop-finder"
    private val varStopArray = "var stopArray"
    private val stopArrayPush = "stopArray.push"
    private val stopArray = "stopArray"

    override fun scrapeStops(): List<AircoachStopJson> {
        val stops = mutableListOf<AircoachStopJson>()
        val document = Jsoup.connect("$aircoachBaseUrl$path").validateTLSCertificates(false).get()
        val scriptElements = document.getElementsByTag("script")
        for (element in scriptElements) {
            var javascript = element.data()
            if (javascript.contains(varStopArray)) {
                javascript = javascript.substring(javascript.indexOf(varStopArray))
                val index = javascript.lastIndexOf(stopArrayPush)
                val endIndex = javascript.indexOf(";", index)
                javascript = javascript.substring(0, endIndex)
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
            }
        }
        if (stops.isEmpty()) {
            throw IllegalStateException("Aircoach stops cannot be empty")
        }
        return stops
    }

}
