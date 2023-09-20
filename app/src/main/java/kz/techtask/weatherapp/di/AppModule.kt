package kz.techtask.weatherapp.di

import kz.techtask.data.repository.CityWeatherRepository
import kz.techtask.weatherapp.ui.CityDetailViewModel
import kz.techtask.weatherapp.ui.CityListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { CityWeatherRepository }

    viewModel { CityListViewModel(repository = get()) }
    viewModel { CityDetailViewModel(repository = get()) }
}
