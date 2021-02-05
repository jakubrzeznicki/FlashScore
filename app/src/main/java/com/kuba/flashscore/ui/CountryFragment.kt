package com.kuba.flashscore.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.FragmentCountryBinding
import com.kuba.flashscore.other.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CountryFragment : Fragment(R.layout.fragment_country) {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlashScoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCountries()
        getAllLeaguesFromSpecificCountry()
        getTeamsFromSpecificLeague()
        getPlayerBySpecificName()
        getStandingsFromSpecificLeague()

        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val countries = result.data
                        countries?.forEach {
                            Timber.d("Country: ${it}")
                        }
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })


        viewModel.leagues.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val league = result.data
                        league?.forEach {
                            Timber.d("League: ${it}")
                        }
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })

        viewModel.teams.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val teams = result.data
                        teams?.forEach {
                            Timber.d("Teams: ${it}")
                        }
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })

        viewModel.players.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val player = result.data
                        player?.forEach {
                            Timber.d("Player: ${it}")
                        }
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })

        viewModel.standings.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val standings = result.data
                        standings?.forEach {
                            Timber.d("Standings: ${it}")
                        }
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }
    private fun getCountries() {
        var job: Job? = null
        binding.buttonCountry.setOnClickListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(2000)
                viewModel.getCountries()
            }
        }
    }

    private fun getAllLeaguesFromSpecificCountry() {
        var job: Job? = null
        binding.buttonLeague.setOnClickListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(2000)
                viewModel.getLeaguesFromSpecificCountry("41")
            }
        }
    }

    private fun getTeamsFromSpecificLeague() {
        var job: Job? = null
        binding.buttonTeams.setOnClickListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(2000)
                viewModel.getTeamsFormSpecificLeague("148")
            }
        }
    }

    private fun getPlayerBySpecificName() {
        var job: Job? = null
        binding.buttonPlayer.setOnClickListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(2000)
                viewModel.getPlayerBySpecificName("Ronaldo Cristiano")
            }
        }
    }

    private fun getStandingsFromSpecificLeague() {
        var job: Job? = null
        binding.buttonStandings.setOnClickListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(2000)
                viewModel.getStandingsFromSpecificLeague("148")
            }
        }
    }
}