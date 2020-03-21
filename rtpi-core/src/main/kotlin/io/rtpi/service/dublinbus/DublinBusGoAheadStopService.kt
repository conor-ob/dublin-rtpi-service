package io.rtpi.service.dublinbus

import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi

class DublinBusGoAheadStopService(
    rtpiApi: RtpiApi
) : AbstractDublinBusStopService(
    rtpiApi = rtpiApi,
    operator = Operator.GO_AHEAD.shortName
)
