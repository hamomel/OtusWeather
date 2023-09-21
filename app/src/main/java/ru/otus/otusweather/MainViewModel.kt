package ru.otus.otusweather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.otus.otusweather.api.WeatherApi
import ru.otus.otusweather.api.WeatherApiProvider
import java.lang.Exception

private const val CITY_ID = "609655"

class MainViewModel(
    private val api: WeatherApi = WeatherApiProvider.getWeatherApi()
) : ViewModel() {
    private val _viewState = MutableStateFlow<MainViewState>(Loading)
    val viewState: Flow<MainViewState> get() = _viewState

    init {
        loadWeather()
    }

    private fun loadWeather() {
        _viewState.value = Loading
        viewModelScope.launch {
            try {
                val weather = api.getWeather(CITY_ID)
                _viewState.value = Data(weather)
            } catch (e: Exception) {
                _viewState.value = Error
                Log.e("MainViewModel", "loading failed", e)
            }
        }
    }

    fun onRefresh() {
        loadWeather()
    }
}