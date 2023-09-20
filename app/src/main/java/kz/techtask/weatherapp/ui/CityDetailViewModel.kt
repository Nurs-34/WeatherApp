package kz.techtask.weatherapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kz.techtask.data.model.CityWeather
import kz.techtask.data.model.Day
import kz.techtask.data.model.Forecastday
import kz.techtask.data.repository.CityWeatherRepository

class CityDetailViewModel(private val repository: CityWeatherRepository) : ViewModel() {
    val cityWeatherActionFlow: MutableSharedFlow<CityListViewModel.CityWeatherAction> = MutableSharedFlow()

    private val _cityForecastFLow = MutableStateFlow<List<Forecastday>>(emptyList())
    val cityForecastFLow = _cityForecastFLow

    private val _cityHistoryFLow = MutableStateFlow<List<Forecastday>>(emptyList())
    val cityHistoryFLow = _cityHistoryFLow

    suspend fun loadCityForecast(city: String) {
        val cityForecastdayList = arrayListOf<Forecastday>()

        try {
            val cityForecastday = repository.getForecastWeather(city).body()
            cityForecastday?.forecast?.forecastday?.forEach { forecast ->
                cityForecastdayList.add(forecast)
            }

            _cityForecastFLow.value = cityForecastdayList
        } catch (e: Exception) {
            Log.e("CityDetailVM", "Exception (forecast): $e")
            cityWeatherActionFlow.emit(CityListViewModel.CityWeatherAction.ShowNetworkErrorToast)
        }
    }

    suspend fun loadCityHistory(city: String) {
        val cityHistoryList = arrayListOf<Forecastday>()

        try {
            Dates.values().forEach { date ->
                val cityHistory = repository.getHistoryWeather(city, date.value)
                    .body()?.forecast?.forecastday?.get(0) //так как в апи истории возвращается массив с одним элементом (то есть днем)
                if (cityHistory != null) {
                    cityHistoryList.add(cityHistory)
                }
            }
            _cityHistoryFLow.value = cityHistoryList
        } catch (e: Exception) {
            Log.e("CityDetailVM", "Exception (history): $e")
            cityWeatherActionFlow.emit(CityListViewModel.CityWeatherAction.ShowNetworkErrorToast)
        }
    }

    sealed class CityWeatherAction {
        data object ShowNetworkErrorToast : CityWeatherAction()
    }

    enum class Dates(val value: String) {
        //захардкодил, так как апи истории возвращает только один день
        TWENTY_ONE("2023-09-21"),
        TWENTY("2023-09-20"),
        NINETEEN("2023-09-19"),
        EIGHTEEN("2023-09-18"),
        SEVENTEEN("2023-09-17"),
        SIXTEEN("2023-09-16"),
        FIFTEEN("2023-09-15")
    }
}
