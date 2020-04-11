package io.rtpi.service.luas

import com.google.inject.Inject
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class LuasLiveDataService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiLiveDataService(
    rtpiApi = rtpiApi,
    operator = Operator.LUAS.shortName
)
