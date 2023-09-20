package kz.techtask.data.model

data class CityWeather(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)