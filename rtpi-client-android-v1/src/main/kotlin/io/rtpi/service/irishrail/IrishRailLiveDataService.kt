package io.rtpi.service.irishrail

import io.rtpi.api.LiveTime
import io.rtpi.resource.irishrail.IrishRailApi
import io.rtpi.resource.irishrail.IrishRailStationDataXml
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import org.threeten.bp.Duration

class IrishRailLiveDataService(irishRailApi: IrishRailApi) : AbstractIrishRailLiveDataService(irishRailApi) {

    override fun createDueTime(xml: IrishRailStationDataXml): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val waitTimeMinutes = xml.dueIn!!.toLong()
        val expectedTime = currentTime.plusMinutes(waitTimeMinutes)
        val waitTimeSeconds = Duration.ofMinutes(waitTimeMinutes).seconds.toInt()
        return LiveTime(waitTimeSeconds, expectedTime.toIso8601())
    }

}
