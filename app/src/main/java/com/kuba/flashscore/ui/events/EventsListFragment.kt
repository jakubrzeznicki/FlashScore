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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.EventAdapter
import com.kuba.flashscore.databinding.FragmentEventsListBinding
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.local.models.entities.CountryWithLeagueAndTeams
import com.kuba.flashscore.local.models.entities.event.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.local.models.entities.event.EventEntity
import com.kuba.flashscore.other.Constants.DATE_FORMAT_DAY_MONTH_YEAR
import com.kuba.flashscore.other.Constants.DATE_FORMAT_DAY_OF_WEEK
import com.kuba.flashscore.other.Constants.DATE_FORMAT_YEAR_MONTH_DAY
import com.kuba.flashscore.other.DateUtils
import com.kuba.flashscore.other.Status
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class EventsListFragment : Fragment(R.layout.fragment_events_list) {

    private var _binding: FragmentEventsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()
    private val args: EventsListFragmentArgs by navArgs()

    private lateinit var eventsAdapter: EventAdapter

    private lateinit var countryAndLeague: CountryAndLeagues
    private lateinit var fromToDate: String
    private var firstFetch = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsListBinding.inflate(inflater, container, false)
        val view = binding.root

        countryAndLeague = args.countryAndLeague
        fromToDate = viewModel.switchedDate.value!!
        Timber.d("POMP ${viewModel.switchedDate.value!!}")
        setTitleAndSubtitle(countryAndLeague,  DateUtils.parseDate(
            fromToDate,
            DATE_FORMAT_YEAR_MONTH_DAY))
        setInformationAboutCountry(countryAndLeague)

        getEvents(
            countryAndLeague.leagues[0].leagueId,
            fromToDate
        )


        setHasOptionsMenu(true)

        goToTableOnClick(countryAndLeague)
        return view
    }

    override fun onResume() {
        super.onResume()
        Timber.d("POMP2 ${viewModel.switchedDate.value!!}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun goToTableOnClick(countryWithLeague: CountryAndLeagues) {
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
            viewLifecycleOwner,
            Observer { items ->
                items?.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            if (result.data != null) {
                                eventsAdapter.events =
                                    result.data.leagueWithEvents[0].events.filter { it?.matchDate == fromToDate }
                                eventsAdapter.countryWithLeagueWithEventsAndTeams = result.data

                            }
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
//                getEvents(
//                    countryAndLeague.leagues[0].leagueId,
//                    fromToDate
//                )
//            }
//        })
    }

    private fun getEvents(leagueId: String, fromTo: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getCountryWithLeagueWithTeamsAndEvents(leagueId, fromTo, fromTo)
            setupRecyclerView()
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
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val date = DateUtils.parseDate(
                    "$dayOfMonth.${month + 1}.$year",
                    DATE_FORMAT_DAY_MONTH_YEAR
                )
                setTitleAndSubtitle(countryAndLeague, date)
                fromToDate = DateUtils.formatDate(date, DATE_FORMAT_YEAR_MONTH_DAY)

                getEvents(
                    leagueId = countryAndLeague.leagues[0].leagueId,
                    DateUtils.formatDate(date, DATE_FORMAT_YEAR_MONTH_DAY)
                )
                viewModel.switchDate(date = DateUtils.formatDate(date, DATE_FORMAT_YEAR_MONTH_DAY))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()

    }

}