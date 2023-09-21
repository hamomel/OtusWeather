package ru.otus.otusweather

import ru.otus.otusweather.api.WeatherResponse

sealed interface MainViewState

data object Loading : MainViewState
data class Data(val weatherResponse: WeatherResponse) : MainViewState
data object Error : MainViewState