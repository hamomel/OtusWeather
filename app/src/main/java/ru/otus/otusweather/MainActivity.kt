package ru.otus.otusweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import ru.otus.otusweather.api.WeatherResponse
import ru.otus.otusweather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.coroutineScope.launch {
            viewModel.viewState
                .flowWithLifecycle(lifecycle)
                .collect { state ->
                    renderState(state)
                }
        }

        binding.swipeLayout.setOnRefreshListener { viewModel.onRefresh() }
    }

    private fun renderState(state: MainViewState) {
        when (state) {
            is Data -> renderData(state.weatherResponse)
            Error -> renderError()
            Loading -> renderLoading()
        }
    }

    private fun renderData(weatherResponse: WeatherResponse) {
        binding.swipeLayout.isRefreshing = false

        binding.cityName.text = weatherResponse.name
        binding.tempTV.text = getString(R.string.temp, weatherResponse.weatherData.temp)
        binding.feelsLikeTV.text = getString(R.string.temp, weatherResponse.weatherData.feelsLike)

        Picasso.get()
            .load(getString(R.string.icon_url, weatherResponse.weatherConditions[0].icon))
            .into(binding.weatherIV)
    }

    private fun renderError() {
        binding.swipeLayout.isRefreshing = false
        Toast.makeText(this, getString(R.string.error_text), Toast.LENGTH_SHORT).show()
    }

    private fun renderLoading() {
        binding.swipeLayout.isRefreshing = false
    }
}