package kz.techtask.data.repository

import kz.techtask.data.model.CityWeather
import kz.techtask.data.network.RestApiInterface
import retrofit2.Response

object CityWeatherRepository {

    private val apiService: RestApiInterface = RestApiInterface()

    suspend fun getCurrentWeather(city: String): Response<CityWeather> {
        return apiService.getCurrentWeather(city)
    }

    suspend fun getForecastWeather(city: String): Response<CityWeather> {
        return apiService.getForecastWeather(city, days = 3)
    }

    suspend fun getHistoryWeather(
        city: String,
        date: String
    ): Response<CityWeather> {
        return apiService.getHistoryWeather(city, date)
    }
}