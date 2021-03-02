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
    private var eventsList: List<EventEntity> = mutableListOf()
    private lateinit var countryWithLeagueAndTeams: CountryWithLeagueAndTeams
    private lateinit var fromToDate: String
    private var firstFetch = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsListBinding.inflate(inflater, container, false)
        val view = binding.root

        countryAndLeague = args.countryAndLeague
        fromToDate = DateUtils.formatDateCurrentDate(DATE_FORMAT_YEAR_MONTH_DAY)

        setTitleAndSubtitle(countryAndLeague, true, null)
        setInformationAboutCountry(countryAndLeague)

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            launch(Dispatchers.Main) {
                getEvents(
                    countryAndLeague.leagues[0].leagueId,
                    fromToDate
                )
            }
        }
//        getCountryWithLeagueAndTeams(
//            countryAndLeague.leagues[0].leagueId
//        )
        setHasOptionsMenu(true)
        Timber.d("JUREKKKK I DEJZIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIiiiii")

        //  goToTableOnClick(countryWithLeagueAndTeams)

//        val dateString =
//            (activity as AppCompatActivity).supportActionBar?.subtitle?.takeWhile { it != ',' }
//
//        leagueId = leagueId
//        fromToDate = DateUtils.parseAndFormatDate(
//            dateString as String,
//            DATE_FORMAT_DAY_MONTH_YEAR,
//            DATE_FORMAT_YEAR_MONTH_DAY
//        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers(countryAndLeague.leagues[0].leagueId, fromToDate)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


//    private fun goToTableOnClick(countryWithLeague: CountryAndLeagues) {
//        binding.apply {
//            imageButtonGoToLeagueTable.setOnClickListener {
//                val action =
//                    EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
//                        countryWithLeague
//                    )
//                it.findNavController().navigate(action)
//            }
//
//            constraintLayoutEventListTable.setOnClickListener {
//                val action =
//                    EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
//                        countryWithLeague
//                    )
//                it.findNavController().navigate(action)
//            }
//        }
//    }

    private fun subscribeToObservers(leagueId: String, fromTo: String) {
        viewModel.teamsFromDb.observe(viewLifecycleOwner, Observer { items ->
            Timber.d("JUREKKKKKK $items")
            //setupRecyclerView(items)
            items?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result != null) {
                            countryWithLeagueAndTeams = result.data!!
                        }
                    }
                    Status.ERROR -> {
                        Timber.d("JUREKKK problem with network, error")
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
            //  eventsAdapter.countryWithLeagueAndTeams = items
            //delay(1000)


        })
        viewModel.eventsFromDb.observe(viewLifecycleOwner, Observer { events ->
            Timber.d("JUREKKKKKK $events")
            //eventsAdapter.events = events
            events?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        eventsList = result.data ?: emptyList()
                    }
                    Status.ERROR -> {
                        Timber.d("JUREKKK problem with network, error")
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
        viewModel.networkConnectivityChange.observe(viewLifecycleOwner, Observer { isNetwork ->
            if (isNetwork) {
                Timber.d("JUREKKK fetch again data")
                //  getEvents(leagueId, fromTo)
            }
        })
        viewModel.eventsFromNetworkStatus.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                    }
                    Status.ERROR -> {
                        Timber.d("JUREKKK problem with network, error")
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


    }

    private fun getEvents(leagueId: String, fromTo: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            val events =
                async { viewModel.getEventsFromSpecificLeague(leagueId, fromTo, fromTo) }.await()
            val teams = async { viewModel.getTeamsFormSpecificLeague(leagueId) }.await()
            Timber.d("JUREKKKKKKKK $leagueId, $fromTo")
            Timber.d("JUREKKK in fragment ")
            setupRecyclerView()

        }
    }
//
//    private fun getCountryWithLeagueAndTeams(leagueId: String) {
//        var job: Job? = null
//        job?.cancel()
//        job = lifecycleScope.launch {
//        }
//    }


    private fun setupRecyclerView() {
        eventsAdapter = EventAdapter()
        eventsAdapter.countryWithLeagueAndTeams = countryWithLeagueAndTeams
        eventsAdapter.events = eventsList
        binding.recyclerViewEvents.apply {
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
        isCurrentDate: Boolean,
        date: Date?
    ) {
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title =
                countryAndLeagues.leagues[0].leagueName.toUpperCase(
                    Locale.ROOT
                )
            subtitle = if (isCurrentDate) {
                "${DateUtils.formatDateCurrentDate(DATE_FORMAT_DAY_MONTH_YEAR)}, ${
                    DateUtils.formatDateCurrentDate(
                        DATE_FORMAT_DAY_OF_WEEK
                    ).toUpperCase()
                }"
            } else {
                "${DateUtils.formatDate(date!!, DATE_FORMAT_DAY_MONTH_YEAR)}, ${
                    DateUtils.formatDate(date, DATE_FORMAT_DAY_OF_WEEK)
                }"
            }
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
                subscribeToObservers(countryAndLeague.leagues[0].leagueId, fromToDate)
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
                setTitleAndSubtitle(countryAndLeague, false, date)
                firstFetch = false
                getEvents(
                    leagueId = countryAndLeague.leagues[0].leagueId,
                    DateUtils.formatDate(date, DATE_FORMAT_YEAR_MONTH_DAY)
                )
//                getCountryWithLeagueAndTeams(
//                    leagueId = countryAndLeague.leagues[0].leagueId
//                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}