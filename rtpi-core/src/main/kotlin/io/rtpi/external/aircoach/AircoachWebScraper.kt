package io.rtpi.external.aircoach

import org.jsoup.Jsoup

interface AircoachWebScraper {

    fun scrapeStops(): List<AircoachStopJson>

}

abstract class AbstractAircoachWebScraper(private val aircoachBaseUrl: String) : AircoachWebScraper {

    private val path = "stop-finder"
    private val varStopArray = "var stopArray"
    private val stopArrayPush = "stopArray.push"
    protected val stopArray = "stopArray"

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
                stops.addAll(scrapeStops(javascript))
            }
        }
        return stops
    }

    abstract fun scrapeStops(javascript: String): List<AircoachStopJson>

}
