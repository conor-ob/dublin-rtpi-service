package io.rtpi.service.dublinbus

import com.google.inject.Inject
import io.rtpi.api.Service
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class DublinBusRtpiLiveDataService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiLiveDataService(
    rtpiApi = rtpiApi,
    operator = "",
    service = Service.DUBLIN_BUS
)
