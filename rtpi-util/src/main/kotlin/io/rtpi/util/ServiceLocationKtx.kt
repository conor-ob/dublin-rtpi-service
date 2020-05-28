package io.rtpi.util

import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation

fun ServiceLocation.directions(): List<String> =
    when {
        isDartStation() -> listOf("Northbound", "Southbound")
        isLuasStop() -> listOf("Inbound", "Outbound")
        else -> emptyList()
    }

private fun ServiceLocation.isDartStation(): Boolean =
    when (id) {
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
        "SUTTN", // Sutton
        "BROCK", // Blackrock
        "GCDK", // Grand Canal Dock
        "GRGRD", // Clongriffin
        "HWTHJ", // Howth Junction
        "LDWNE", // Lansdowne Road
        "PMNCK", // Portmarnock
        "BRAY", // Bray
        "CNLLY", // Dublin Connolly
        "DLERY", // Dun Laoghaire
        "GSTNS", // Greystones
        "MHIDE", // Malahide
        "PERSE", // Dublin Pearse
        "TARA" // Tara Street
        -> true
        else -> false
    }

private fun ServiceLocation.isLuasStop(): Boolean = service == Service.LUAS
