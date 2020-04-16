package io.rtpi.service.buseireann

import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.Operator
import io.rtpi.api.Service
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class BusEireannLiveDataService @Inject constructor(
    @Named("rtpi_api") rtpiApi: RtpiApi,
    @Named("rtpi_fallback_api") rtpiFallbackApi: RtpiApi
) : AbstractRtpiLiveDataService(
    rtpiApi = rtpiApi,
    rtpiFallbackApi = rtpiFallbackApi,
    operator = Operator.BUS_EIREANN.shortName,
    service = Service.BUS_EIREANN
)
