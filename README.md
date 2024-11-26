# Dublin RTPI Service

![Dublin RTPI Service CI](https://github.com/conor-ob/dublin-rtpi-service/workflows/Dublin%20RTPI%20Service%20CI/badge.svg)
[![codecov](https://codecov.io/gh/conor-ob/dublin-rtpi-service/branch/master/graph/badge.svg?token=CFG1R8086X)](https://codecov.io/gh/conor-ob/dublin-rtpi-service)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

Dropwizard service written in Kotlin to provide a unified API to aggregate data from Dublin public transport APIs into a common canonical data model.

Swagger docs and REST playground [here](https://dublin-rtpi.herokuapp.com/swagger.html). The Heroku container sleeps after 30 minutes of inactivity so it may need a few seconds to start up.

Example requests
* GET all Irish Rail stations https://dublin-rtpi.herokuapp.com/irishrail/locations
* GET all Aircoach stops https://dublin-rtpi.herokuapp.com/aircoach/locations
* GET live data for Dublin Bus stop 315 (Bachelor's Walk) https://dublin-rtpi.herokuapp.com/dublinbus/livedata?locationId=315
* GET live data for Connolly station https://dublin-rtpi.herokuapp.com/irishrail/livedata?locationId=CNLLY
* GET live data for St. Stephen's Green Luas https://dublin-rtpi.herokuapp.com/luas/livedata?locationId=LUAS24

Supported Services
* Dublin Bus / Go Ahead
* Irish Rail (DART, Commuter and InterCity)
* Luas
* Bus Éireann
* Aircoach
* Dublin Bikes (API key [required](https://developer.jcdecaux.com/#/opendata/vls?page=getstarted))
