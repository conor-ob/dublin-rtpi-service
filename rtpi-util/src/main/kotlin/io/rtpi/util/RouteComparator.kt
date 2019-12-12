package io.rtpi.util

import io.rtpi.api.Route

object RouteComparator : Comparator<Route> {

    override fun compare(r1: Route, r2: Route): Int = AlphanumComparator.getInstance().compare(r1.id, r2.id)

}
