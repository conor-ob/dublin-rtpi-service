package io.rtpi.service.dublinbus

import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.Service
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class DublinBusRtpiLiveDataService @Inject constructor(
    @Named("rtpi_api") rtpiApi: RtpiApi,
    @Named("rtpi_fallback_api") rtpiFallbackApi: RtpiApi
) : AbstractRtpiLiveDataService(
    rtpiApi = rtpiApi,
    rtpiFallbackApi = rtpiFallbackApi,
    operator = "",
    service = Service.DUBLIN_BUS
)
