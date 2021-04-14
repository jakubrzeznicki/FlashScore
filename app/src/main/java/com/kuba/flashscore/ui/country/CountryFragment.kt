package com.kuba.flashscore.ui.country

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.CountryAdapter
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.databinding.FragmentCountryBinding
import com.kuba.flashscore.other.Constants.COUNTRIES
import com.kuba.flashscore.other.Status
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CountryFragment @Inject constructor(
    val countryAdapter: CountryAdapter
) : Fragment(R.layout.fragment_country) {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: CountryViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        val view = binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarCountry)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            title = COUNTRIES
            subtitle = ""
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(CountryViewModel::class.java)

        viewModel.getCountries()
        setupRecyclerView()
        subscribeToObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun subscribeToObservers() {
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            Timber.d("COUNTRYY gert form db")
            if (it.isNullOrEmpty()) {
                Timber.d("COUNTRYY gert form db are null")
                refreshCountries()
            } else {
                Timber.d("COUNTRYY gert form db are not null $it")
                countryAdapter.country = it
            }
        })
        viewModel.countriesStatus.observe(viewLifecycleOwner, Observer {
            Timber.d("COUNTRYY gert form network")
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView(),
                            "Successfully fetched data from network",
                            Snackbar.LENGTH_LONG
                        ).show()
                        viewModel?.getCountries()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireView(),
                            result.message ?: "Default No Internet",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })

//        viewModel.networkConnectivityChange.observe(viewLifecycleOwner, Observer { isNetwork ->
//            if (isNetwork) {
//                refreshCOuntries()
//            }
//        })
    }

    private fun refreshCountries() {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.refreshCountries()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCountries.apply {
            adapter = countryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                HorizontalDividerItemDecoration.Builder(requireContext())
                    .color(ContextCompat.getColor(requireContext(), R.color.secondaryTextColor))
                    .sizeResId(R.dimen.divider)
                    .build()
            )
        }
    }
}