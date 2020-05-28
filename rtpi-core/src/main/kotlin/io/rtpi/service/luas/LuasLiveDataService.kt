package io.rtpi.service.luas

import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.Operator
import io.rtpi.api.Service
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class LuasLiveDataService @Inject constructor(
    @Named("rtpi_api") rtpiApi: RtpiApi,
    @Named("rtpi_fallback_api") rtpiFallbackApi: RtpiApi
) : AbstractRtpiLiveDataService(
    rtpiApi = rtpiApi,
    rtpiFallbackApi = rtpiFallbackApi,
    operator = Operator.LUAS.shortName,
    service = Service.LUAS
)
