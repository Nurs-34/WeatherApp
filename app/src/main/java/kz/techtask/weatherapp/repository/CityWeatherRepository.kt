package kz.techtask.weatherapp.repository

import kz.techtask.weatherapp.model.CityWeather
import kz.techtask.weatherapp.network.RestApiInterface
import retrofit2.Response

object CityWeatherRepository {

    private val apiService: RestApiInterface = RestApiInterface()

    suspend fun getCurrentWeather(city: String): Response<CityWeather> {
        return apiService.getCurrentWeather(city)
    }

    suspend fun getForecastWeather(city: String): Response<CityWeather> {
        return apiService.getForecastWeather(city, days = 3)
    }

    suspend fun getHistoryWeather(city: String, date: String): Response<CityWeather> {
        return apiService.getHistoryWeather(city, date)
    }
}