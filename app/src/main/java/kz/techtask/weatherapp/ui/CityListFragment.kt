package kz.techtask.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kz.techtask.weatherapp.R
import kz.techtask.weatherapp.adapters.CityListAdapter
import kz.techtask.weatherapp.databinding.FragmentCityDetailBinding
import kz.techtask.weatherapp.databinding.FragmentCityListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CityListFragment : Fragment() {

    private var _binding: FragmentCityListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CityListAdapter
    private val viewModel: CityListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lifecycleScope = viewLifecycleOwner.lifecycleScope

        adapter = CityListAdapter(emptyList()) {
            val action =
                CityListFragmentDirections.navigateToCityWeatherDetail(
                    city = it.location.name,
                    date = it.location.localtime
                )
            Navigation.findNavController(view).navigate(action)
        }

        lifecycleScope.launch {
            showProgressBar()
            viewModel.loadCitiesWeather()
            adapter.updateList(viewModel.cityListFLow.value)
            showRecyclerView()
        }

        binding.list.adapter = adapter

        viewModel.cityWeatherActionFlow.onEach { action ->
            when (action) {
                is CityListViewModel.CityWeatherAction.ShowNetworkErrorToast -> Toast.makeText(
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

