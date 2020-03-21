package io.rtpi.service.irishrail

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.IrishRailStation
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.irishrail.IrishRailStationResponseXml
import io.rtpi.external.irishrail.IrishRailStationXml

class IrishRailStationService @Inject constructor(private val irishRailApi: IrishRailApi) {

    fun getStations(): Single<List<IrishRailStation>> {
        return irishRailApi.getAllStationsXml()
            .map { responseXml -> filterStations(responseXml) }
            .map { filteredXml -> mapStations(filteredXml) }
    }

    private fun filterStations(responseXml: IrishRailStationResponseXml): List<IrishRailStationXml> {
        return responseXml.stations!!.filter { xml ->
            xml.code != null && filterDuplicates(xml)
                && xml.name != null
                && xml.latitude != null
                && xml.longitude != null
                && xml.latitude != 0.0
                && xml.longitude != 0.0
        }
    }

    private fun filterDuplicates(xml: IrishRailStationXml): Boolean {
        return xml.code != "ADAMF" && xml.code != "ADAMS" // Adamstown
            && xml.code != "CLONF" && xml.code != "CLONS" // Clondalkin
            && xml.code != "HAZEF" && xml.code != "HAZES" // Hazelhatch
            && xml.code != "PWESF" && xml.code != "PWESS" // Park West and Cherry Orchard
    }

    private fun mapStations(filteredXml: List<IrishRailStationXml>): List<IrishRailStation>? {
        return filteredXml.map { xml ->
            val id = xml.code!!.trim().toUpperCase()
            val operators = mapOperators(id)
            IrishRailStation(
                id = id,
                name = xml.name!!.trim(),
                coordinate = Coordinate(xml.latitude!!, xml.longitude!!),
                operators = operators,
                routes = operators.map { Route(it.fullName, it) }
            )
        }
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
