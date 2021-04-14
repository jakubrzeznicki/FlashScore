package com.kuba.flashscore.ui.league

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.LeagueAdapter
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.databinding.FragmentLeagueBinding
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.country.CountryViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LeagueFragment : Fragment(R.layout.fragment_league) {

    private var _binding: FragmentLeagueBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: LeagueViewModel
    private val args: LeagueFragmentArgs by navArgs()

    lateinit var leagueAdapter: LeagueAdapter
    private lateinit var country: Country

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLeagueBinding.inflate(inflater, container, false)
        val view = binding.root

        country = args.countryItem

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = country.countryName
            subtitle = ""
        }
        setInformationAboutCountry(country)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LeagueViewModel::class.java)

        viewModel.getCountryWithLeagues(country.countryId)
        setupRecyclerView()
        subscribeToObservers()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun subscribeToObservers() {
        viewModel.countriesWithLeagues.observe(viewLifecycleOwner, Observer {
            Timber.d("LEAGUEE observe get leagues")
            if (it?.leagues.isNullOrEmpty()) {
                Timber.d("LEAGUE gert form db are null")
                refreshCountryWithLeagues(country)
            } else {
                Timber.d("LEAGUE gert form db are not null ${it.leagues}")
                leagueAdapter.league = it.leagues
            }
        })
        viewModel.countryWithLeaguesStatus.observe(viewLifecycleOwner, Observer {
            Timber.d("LEAGUEE observe refresh leagues")
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView(),
                            "Successfully fetched data from network",
                            Snackbar.LENGTH_LONG
                        ).show()
                        viewModel.getCountryWithLeagues(country.countryId)
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
//                getLeagues(country)
//            }
//        })

    }

    private fun refreshCountryWithLeagues(country: Country) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.refreshCountryWithLeagues(country.countryId)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewLeagues.apply {
            leagueAdapter = LeagueAdapter(country)
            adapter = leagueAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                HorizontalDividerItemDecoration.Builder(requireContext())
                    .color(ContextCompat.getColor(requireContext(), R.color.secondaryTextColor))
                    .sizeResId(R.dimen.divider)
                    .build()
            )
        }
    }

    private fun setInformationAboutCountry(country: Country) {
        binding.apply {
            textViewCountryName.text = country.countryName
            Glide.with(requireContext()).load(country.countryLogo).into(imageViewCountryFlag)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}