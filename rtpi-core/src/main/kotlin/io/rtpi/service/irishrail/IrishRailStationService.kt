package io.rtpi.service.irishrail

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.RouteGroup
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation
import io.rtpi.api.StopLocation
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.irishrail.IrishRailStationResponseXml
import io.rtpi.external.irishrail.IrishRailStationXml
import io.rtpi.validation.validate
import io.rtpi.validation.validateDoubles
import io.rtpi.validation.validateStrings

class IrishRailStationService @Inject constructor(private val irishRailApi: IrishRailApi) {

    fun getStations(): Single<List<ServiceLocation>> {
        return irishRailApi
            .getAllStationsXml()
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: IrishRailStationResponseXml): List<ServiceLocation> =
        if (response.stations.isNullOrEmpty()) {
            emptyList()
        } else {
            requireNotNull(response.stations)
                .filter { xml ->
                    validateStrings(xml.code, xml.name) &&
                        validateDoubles(xml.latitude, xml.longitude) &&
                        filterDuplicates(xml)
                }.map { xml ->
                    val id = xml.code.validate().toUpperCase()
                    val operators = mapOperators(id)
                    StopLocation(
                        id = id,
                        name = xml.name.validate(),
                        service = Service.IRISH_RAIL,
                        coordinate = Coordinate(xml.latitude.validate(), xml.longitude.validate()),
                        routes = operators.map { operator ->
                            RouteGroup(operator, listOf(operator.fullName))
                        },
                        properties = mutableMapOf()
                    )
                }
        }

    private fun filterDuplicates(xml: IrishRailStationXml): Boolean {
        return xml.code != "ADAMF" && xml.code != "ADAMS" // Adamstown
            && xml.code != "CLONF" && xml.code != "CLONS" // Clondalkin
            && xml.code != "HAZEF" && xml.code != "HAZES" // Hazelhatch
            && xml.code != "PWESF" && xml.code != "PWESS" // Park West and Cherry Orchard
    }

    // https://www.irishrail.ie/Travel-Information/Station-and-Route-maps/Dublin-Symbolic-Map
    // https://moovitapp.com/ireland-502/lines/en?utm_source=seo_lines&customerId=4908
    private fun mapOperators(stationId: String): Set<Operator> {
        return when (stationId) {
            "BTSTN", // Booterstown
            "BYSDE", // Bayside
            "CTARF", // Clontarf Road
            "DLKEY", // Dalkey
            "GLGRY", // Glenageary
            "HTOWN", // Harmonstown
            "HOWTH", // Howth
            "KBRCK", // Kilbarrack
            "KILNY", // Killiney
            "KLSTR", // Killester
            "RAHNY", // Raheny
            "SCOVE", // Sandycove
            "SEAPT", // Seapoint
            "SHILL", // Salthill and Monkstown
            "SIDNY", // Sydney Parade
            "SKILL", // Shankill
            "SMONT", // Sandymount
            "SUTTN"  // Sutton
            -> setOf(Operator.DART)

//            "ADAMF", // Adamstown
//            "ADAMS", // Adamstown
            "ADMTN", // Adamstown
            "ASHTN", // Ashtown
            "ATLNE", // Athlone
            "BBRDG", // Broombridge
            "BBRGN", // Balbriggan
            "CLARA", // Clara
            "CLDKN", // Clondalkin
//            "CLONF", // Clondalkin
//            "CLONS", // Clondalkin
            "CLSLA", // Clonsilla
            "CMINE", // Coolmine
            "CNOCK", // Castleknock
            "DCKLS", // Docklands
            "DBATE", // Donabate
            "DBYNE", // Dunboyne
            "DDALK", // Dundalk
            "DGHDA", // Drogheda
            "GSTON", // Gormanston
            "HAFLD", // Hansfield
//            "HAZEF", // Hazelhatch
//            "HAZES", // Hazelhatch
            "HZLCH", // Hazelhatch
            "LTOWN", // Laytown
            "LXCON", // Leixlip (Confey)
            "LXLSA", // Leixlip (Louisa Bridge)
            "M3WAY", // M3 Parkway
            "PHNPK", // Navan Road Parkway
//            "PWESF", // Park West and Cherry Orchard
//            "PWESS", // PARK WEST
            "CHORC", // Park West and Cherry Orchard
            "RLUSK", // Rush and Lusk
            "SKRES", // Skerries
            "TMORE"  // Tullamore
            -> setOf(Operator.COMMUTER)

            "BROCK", // Blackrock
            "GCDK",  // Grand Canal Dock
            "GRGRD", // Clongriffin
            "HWTHJ", // Howth Junction
            "LDWNE", // Lansdowne Road
            "PMNCK"  // Portmarnock
            -> setOf(Operator.COMMUTER, Operator.DART)

            "ARKLW", // Arklow
            "ATHY",  // Athy
            "CRLOW", // Carlow
            "DCDRA", // Drumcondra
            "ECRTY", // Enniscorthy
            "ENFLD", // Enfield
            "ETOWN", // Edgeworthstown
            "HSTON", // Dublin Heuston
            "GOREY", // Gorey
            "KCOCK", // Kilcock
            "KCOOL", // Kilcoole
            "KDARE", // Kildare
            "LFORD", // Longford
            "MLGAR", // Mullingar
            "MONVN", // Monasterevin
            "MYNTH", // Maynooth
            "NBRGE", // Newbridge
            "PTRTN", // Portarlington
            "PTLSE", // Portlaoise
            "RDRUM", // Rathdrum
            "SALNS", // Sallins
            "WLOW"   // Wicklow
            -> setOf(Operator.COMMUTER, Operator.INTERCITY)

            "BRAY",  // Bray
            "CNLLY", // Dublin Connolly
            "DLERY", // Dun Laoghaire
            "GSTNS", // Greystones
            "MHIDE", // Malahide
            "PERSE", // Dublin Pearse
            "TARA"   // Tara Street
            -> setOf(Operator.COMMUTER, Operator.DART, Operator.INTERCITY)

            else -> setOf(Operator.INTERCITY)
        }
    }
}
