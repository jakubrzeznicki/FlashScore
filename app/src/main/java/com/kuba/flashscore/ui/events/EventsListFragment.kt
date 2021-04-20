package com.kuba.flashscore.ui.events

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.EventAdapter
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.domain.models.event.customs.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.databinding.FragmentEventsListBinding
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.other.Constants.DATE_FORMAT_DAY_MONTH_YEAR
import com.kuba.flashscore.other.Constants.DATE_FORMAT_DAY_OF_WEEK
import com.kuba.flashscore.other.Constants.DATE_FORMAT_YEAR_MONTH_DAY
import com.kuba.flashscore.other.Constants.DEFAULT_ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.SUCCESS_MESSAGE
import com.kuba.flashscore.other.DateUtils
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.league.LeagueViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_events_list.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class EventsListFragment : Fragment(R.layout.fragment_events_list) {

    private var _binding: FragmentEventsListBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: EventsViewModel
    private val args: EventsListFragmentArgs by navArgs()

    lateinit var eventsAdapter: EventAdapter
    private var wasRefresh = false
    private lateinit var countryAndLeague: CountryAndLeagues
    private lateinit var fromToDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsListBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(requireActivity()).get(EventsViewModel::class.java)

        countryAndLeague = args.countryAndLeagues
        fromToDate = viewModel.switchedDate.value!!

        setTitleAndSubtitle(
            countryAndLeague, DateUtils.parseDate(
                fromToDate,
                DATE_FORMAT_YEAR_MONTH_DAY
            )
        )
        setInformationAboutCountry(countryAndLeague)

        setHasOptionsMenu(true)

        goToTeamsListAndStandings(countryAndLeague)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCountryWithLeagueWithEventsAndTeams(
            countryAndLeague.leagues[0].leagueId,
            fromToDate
        )
        setupRecyclerView()
        subscribeToObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun goToTeamsListAndStandings(countryWithLeague: CountryAndLeagues) {
        binding.apply {
            imageButtonGoToLeagueTable.setOnClickListener {
                val action =
                    EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
                        countryWithLeague
                    )
                it.findNavController().navigate(action)
            }

            constraintLayoutEventListTable.setOnClickListener {
                val action =
                    EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
                        countryWithLeague
                    )
                it.findNavController().navigate(action)
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.countryWithLeagueWithTeamsAndEvents.observe(
            viewLifecycleOwner, Observer {
                Timber.d("EVENTS observe get events")
                if (it == null) {
                    Timber.d("EVENTS gert form db are null $wasRefresh")
                    //if (!wasRefresh) {
                        refreshEvents(countryAndLeague.leagues[0].leagueId, fromToDate)
                    //}
                    setupRecyclerView()
                } else {
                    Timber.d("EVENTS gert form db are not null ${it.leagueWithEvents[0].eventsWithEventInformation.size}")
                    eventsAdapter.countryWithLeagueWithEventsAndTeams = it
                    eventsAdapter.events =
                        it.leagueWithEvents[0].eventsWithEventInformation.filter { event ->
                            event.event.matchDate == fromToDate
                        }
                }
            }
        )
        viewModel.countryWithLeagueWithTeamsAndEventsEntityStatus.observe(
            viewLifecycleOwner, Observer { items ->
                items?.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            Snackbar.make(
                                requireView(),
                                result.message ?: SUCCESS_MESSAGE,
                                Snackbar.LENGTH_LONG
                            ).show()
                            viewModel.getCountryWithLeagueWithEventsAndTeams(countryAndLeague.leagues[0].leagueId, fromToDate)
                        }
                        Status.ERROR -> {
                            Snackbar.make(
                                requireView(),
                                result.message ?: DEFAULT_ERROR_MESSAGE,
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
//                getEvents(
//                    countryAndLeague.leagues[0].leagueId,
//                    fromToDate
//                )
//            }
//        })
    }

    private fun refreshEvents(leagueId: String, fromTo: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.refreshEvents(leagueId, fromTo)
           // wasRefresh = true
            //delay(1000)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewEvents.apply {
            eventsAdapter = EventAdapter()
            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                HorizontalDividerItemDecoration.Builder(requireContext())
                    .color(ContextCompat.getColor(requireContext(), R.color.secondaryTextColor))
                    .sizeResId(R.dimen.divider)
                    .build()
            )
        }
    }

    private fun setTitleAndSubtitle(
        countryAndLeagues: CountryAndLeagues,
        date: Date?
    ) {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarEventList)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title =
                countryAndLeagues.leagues[0].leagueName.toUpperCase(
                    Locale.ROOT
                )
            subtitle =
                "${DateUtils.formatDate(date!!, DATE_FORMAT_DAY_MONTH_YEAR)}, ${
                    DateUtils.formatDate(date, DATE_FORMAT_DAY_OF_WEEK)
                }"
        }

    }

    private fun setInformationAboutCountry(countryAndLeagues: CountryAndLeagues) {
        binding.apply {
            textViewCountryName.text = countryAndLeagues.country.countryName
            textViewLeagueName.text = countryAndLeagues.leagues[0].leagueName
            Glide.with(requireContext())
                .load(countryAndLeagues.country.countryLogo)
                .into(imageViewCountryFlag)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_event_date, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
            R.id.actionMenuPickDate -> {
                setDatePickerDialog(requireContext())
                subscribeToObservers()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat")
    fun setDatePickerDialog(
        context: Context
    ) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.DialogTheme,
            { _, year, month, dayOfMonth ->
                val date = DateUtils.parseDate(
                    "$dayOfMonth.${month + 1}.$year",
                    DATE_FORMAT_DAY_MONTH_YEAR
                )
                setTitleAndSubtitle(countryAndLeague, date)
                fromToDate = DateUtils.formatDate(date, DATE_FORMAT_YEAR_MONTH_DAY)

                refreshEvents(
                    leagueId = countryAndLeague.leagues[0].leagueId,
                    DateUtils.formatDate(date, DATE_FORMAT_YEAR_MONTH_DAY)
                )
                viewModel.switchDate(date = DateUtils.formatDate(date, DATE_FORMAT_YEAR_MONTH_DAY))
                //wasRefresh = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()

    }

}