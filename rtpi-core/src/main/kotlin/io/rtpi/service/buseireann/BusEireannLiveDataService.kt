package io.rtpi.service.buseireann

import com.google.inject.Inject
import io.rtpi.api.Operator
import io.rtpi.api.Service
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class BusEireannLiveDataService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiLiveDataService(
    rtpiApi = rtpiApi,
    operator = Operator.BUS_EIREANN.shortName,
    service = Service.BUS_EIREANN
)
