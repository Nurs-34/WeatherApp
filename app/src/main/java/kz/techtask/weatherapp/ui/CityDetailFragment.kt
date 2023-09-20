package kz.techtask.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kz.techtask.weatherapp.R
import kz.techtask.weatherapp.ui.adapters.CityDetailAdapter
import kz.techtask.weatherapp.databinding.FragmentCityDetailBinding
import kz.techtask.weatherapp.ui.CityDetailViewModel.CityWeatherAction.ShowNetworkErrorToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class CityDetailFragment : Fragment() {

    private var _binding: FragmentCityDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CityDetailAdapter
    private val viewModel: CityDetailViewModel by viewModel()

    private val args: CityDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lifecycleScope = viewLifecycleOwner.lifecycleScope
        val city = args.city
        binding.tvCity.text = city

        adapter = CityDetailAdapter(emptyList())

        lifecycleScope.launch {
            showProgressBar()
            viewModel.loadCityForecast(city)
            adapter.updateList(viewModel.cityForecastFLow.value)
            showRecyclerView()
        }

        binding.list.adapter = adapter

        binding.btnForecast.setOnClickListener {
            lifecycleScope.launch {
                showProgressBar()
                viewModel.loadCityForecast(city)
                adapter.updateList(viewModel.cityForecastFLow.value)
                showRecyclerView()
            }
        }

        binding.btnHistory.setOnClickListener {
            lifecycleScope.launch {
                showProgressBar()
                viewModel.loadCityHistory(city)
                adapter.updateList(viewModel.cityHistoryFLow.value)
                showRecyclerView()
            }
        }

        viewModel.cityWeatherActionFlow.onEach { action ->
            when (action) {
                is ShowNetworkErrorToast -> Toast.makeText(
                    requireContext(),
                    getString(R.string.please_check_your_internet_connection),
                    Toast.LENGTH_LONG
                ).show()
            }
        }.launchIn(lifecycleScope)
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.list.visibility = View.GONE
    }

    private fun showRecyclerView() {
        binding.progressBar.visibility = View.GONE
        binding.list.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}