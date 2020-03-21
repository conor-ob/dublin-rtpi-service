package io.rtpi.service.dublinbus

import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi

class DublinBusDublinBusStopService(
    rtpiApi: RtpiApi
) : AbstractDublinBusStopService(
    rtpiApi = rtpiApi,
    operator = Operator.DUBLIN_BUS.shortName
)
