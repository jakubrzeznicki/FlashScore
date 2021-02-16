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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.EventAdapter
import com.kuba.flashscore.databinding.FragmentEventsListBinding
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.other.Constants.DATE_FORMAT_DAY_MONTH_YEAR
import com.kuba.flashscore.other.Constants.DATE_FORMAT_DAY_OF_WEEK
import com.kuba.flashscore.other.Constants.DATE_FORMAT_YEAR_MONTH_DAY
import com.kuba.flashscore.other.Status
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class EventsListFragment : Fragment(R.layout.fragment_events_list) {

    private var _binding: FragmentEventsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()
    private lateinit var eventsAdapter: EventAdapter

    private lateinit var league: LeagueDto

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormatDayMonthYear = SimpleDateFormat(DATE_FORMAT_DAY_MONTH_YEAR)

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormatDayOfWeek = SimpleDateFormat(DATE_FORMAT_DAY_OF_WEEK)

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormatYearMonthDay = SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DAY)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsListBinding.inflate(inflater, container, false)
        val view = binding.root

        league = EventsListFragmentArgs.fromBundle(requireArguments()).leagueItem

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = league.leagueName.toUpperCase(Locale.ROOT)
            subtitle = "${simpleDateFormatDayMonthYear.format(Date(System.currentTimeMillis()))}, ${
                simpleDateFormatDayOfWeek.format(Date(System.currentTimeMillis()))
                    .toUpperCase(Locale.ROOT)
            }"
        }
        goToTableOnClick(league)
        setInformationAboutCountry(league)

        val dateString =
            (activity as AppCompatActivity).supportActionBar?.subtitle?.takeWhile { it != ',' }
        val date = simpleDateFormatDayMonthYear.parse(dateString as String)
        val formattedDate = simpleDateFormatYearMonthDay.format(date)
        getEvents(league.leagueId, formattedDate)
        return view
    }

    private fun goToTableOnClick(league: LeagueDto) {
        binding.apply {
            imageButtonGoToLeagueTable.setOnClickListener {
                val action =
                    EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
                        league
                    )
                it.findNavController().navigate(action)
            }

            constraintLayoutEventListTable.setOnClickListener {
                val action =
                    EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
                        league
                    )
                it.findNavController().navigate(action)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun subscribeToObservers() {
        viewModel.events.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val events = result.data
                        if (events != null) {
                            eventsAdapter.events = events
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

    private fun getEvents(leagueId: String, fromTo: String) {
        var job: Job? = null
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getEventsFromSpecificLeague(leagueId, fromTo, fromTo)
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

    private fun setInformationAboutCountry(league: LeagueDto) {
        binding.apply {
            textViewCountryName.text = league.countryName
            Glide.with(requireContext()).load(league.countryLogo).into(imageViewCountryFlag)
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
                setDatePickerDialog(requireContext(), league)
                subscribeToObservers()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat")
    fun setDatePickerDialog(context: Context, league: LeagueDto) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val date = simpleDateFormatDayMonthYear.parse("$dayOfMonth.${month + 1}.$year")
                (activity as AppCompatActivity).supportActionBar?.apply {
                    title = league.leagueName.toUpperCase(Locale.ROOT)
                    subtitle =
                        "${simpleDateFormatDayMonthYear.format(date)}, ${
                            simpleDateFormatDayOfWeek.format(
                                date
                            ).toUpperCase(Locale.ROOT)
                        }"
                }
                getEvents(leagueId = league.leagueId, simpleDateFormatYearMonthDay.format(date))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


}