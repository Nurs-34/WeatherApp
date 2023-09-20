package kz.techtask.weatherapp.model

data class CityWeather(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)