package io.rtpi.external.irishrail

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ArrayOfObjStation")
data class IrishRailStationResponseXml(
    @field:ElementList(name = "objStation", inline = true, required = false) var stations: List<IrishRailStationXml>? = null
)

@Root(name = "objStation")
data class IrishRailStationXml(
    @field:Element(name = "StationDesc", required = false) var name: String? = null,
    @field:Element(name = "StationAlias", required = false) var alias: String? = null,
    @field:Element(name = "StationLatitude", required = false) var latitude: Double? = null,
    @field:Element(name = "StationLongitude", required = false) var longitude: Double? = null,
    @field:Element(name = "StationCode", required = false) var code: String? = null,
    @field:Element(name = "StationId", required = false) var id: String? = null
)
