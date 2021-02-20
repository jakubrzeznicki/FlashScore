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
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.other.Constants.DATE_FORMAT_DAY_MONTH_YEAR
import com.kuba.flashscore.other.Constants.DATE_FORMAT_DAY_OF_WEEK
import com.kuba.flashscore.other.Constants.DATE_FORMAT_YEAR_MONTH_DAY
import com.kuba.flashscore.other.DateUtils
import com.kuba.flashscore.other.Status
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class EventsListFragment : Fragment(R.layout.fragment_events_list) {

    private var _binding: FragmentEventsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()
    private lateinit var eventsAdapter: EventAdapter

    private lateinit var countryAndLeague: CountryAndLeagues

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsListBinding.inflate(inflater, container, false)
        val view = binding.root
        //private val args: GalleryFragmentArgs by navArgs()

        val countryAndLeague =
            EventsListFragmentArgs.fromBundle(requireArguments()).countryAndLeagueItem

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = countryAndLeague.leagues[0].leagueName.toUpperCase(Locale.ROOT)
            subtitle = "${DateUtils.formatDateCurrentDate(DATE_FORMAT_DAY_MONTH_YEAR)}, ${
                DateUtils.formatDateCurrentDate(
                    DATE_FORMAT_DAY_OF_WEEK
                ).toUpperCase()
            }"
        }
        goToTableOnClick(countryAndLeague)
        setInformationAboutCountry(countryAndLeague)

        val dateString =
            (activity as AppCompatActivity).supportActionBar?.subtitle?.takeWhile { it != ',' }
        getEvents(
            countryAndLeague.leagues[0].leagueId,
            DateUtils.parseAndFormatDate(
                dateString as String,
                DATE_FORMAT_DAY_MONTH_YEAR,
                DATE_FORMAT_YEAR_MONTH_DAY
            )
        )
        return view
    }

    private fun goToTableOnClick(countryAndLeagues: CountryAndLeagues) {
        binding.apply {
            imageButtonGoToLeagueTable.setOnClickListener {
                val action =
                    EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
                        countryAndLeagues
                    )
                it.findNavController().navigate(action)
            }

            constraintLayoutEventListTable.setOnClickListener {
                val action =
                    EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
                        countryAndLeagues
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

    private fun setInformationAboutCountry(countryAndLeagues: CountryAndLeagues) {
        binding.apply {
            textViewCountryName.text = countryAndLeagues.country.countryName
            Glide.with(requireContext()).load(countryAndLeagues.country.countryLogo)
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
                setDatePickerDialog(requireContext(), countryAndLeague)
                subscribeToObservers()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat")
    fun setDatePickerDialog(context: Context, countryAndLeagues: CountryAndLeagues) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val date = DateUtils.parseDate(
                    "$dayOfMonth.${month + 1}.$year",
                    DATE_FORMAT_DAY_MONTH_YEAR
                )
                (activity as AppCompatActivity).supportActionBar?.apply {
                    title = countryAndLeagues.leagues[0].leagueName.toUpperCase(Locale.ROOT)
                    subtitle =
                        "${DateUtils.formatDate(date, DATE_FORMAT_DAY_MONTH_YEAR)}, ${
                            DateUtils.formatDate(date, DATE_FORMAT_DAY_OF_WEEK)
                        }"
                }
                getEvents(
                    leagueId = countryAndLeagues.leagues[0].leagueId,
                    DateUtils.formatDate(date, DATE_FORMAT_YEAR_MONTH_DAY)
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


}