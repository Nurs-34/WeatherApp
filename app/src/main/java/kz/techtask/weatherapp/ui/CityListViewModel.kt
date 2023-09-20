package kz.techtask.weatherapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kz.techtask.data.model.CityWeather
import kz.techtask.data.repository.CityWeatherRepository
import kz.techtask.weatherapp.ui.CityListViewModel.CityWeatherAction.ShowNetworkErrorToast

class CityListViewModel(private val repository: CityWeatherRepository) : ViewModel() {
    val cityWeatherActionFlow: MutableSharedFlow<CityWeatherAction> = MutableSharedFlow()

    private val _cityListFLow = MutableStateFlow<List<CityWeather>>(emptyList())
    val cityListFLow = _cityListFLow

    suspend fun loadCitiesWeather() {
        val cityWeatherList = arrayListOf<CityWeather>()
        try {
            Cities.values().forEach { city ->
                val cityWeather =
                    repository.getCurrentWeather(city.value).body() as CityWeather
                cityWeatherList.add(cityWeather)
            }

            _cityListFLow.value = cityWeatherList
        } catch (e: Exception) {
            Log.e("CityListVM", "Exception: $e")
            cityWeatherActionFlow.emit(ShowNetworkErrorToast)
        }
    }

    sealed class CityWeatherAction {
        data object ShowNetworkErrorToast : CityWeatherAction()
    }

    enum class Cities(val value: String) {
        ASTANA("Astana"),
        BISHKEK("Bishkek"),
        LONDON("London"),
        MOSCOW("Moscow"),
        PARIS("Paris"),
        TOKYO("Tokyo"),
        WASHINGTON("Washington")
    }
}
