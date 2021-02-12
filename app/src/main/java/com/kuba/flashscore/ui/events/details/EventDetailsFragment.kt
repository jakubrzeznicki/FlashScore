package com.kuba.flashscore.ui.events.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.EventDetailAdapter
import com.kuba.flashscore.databinding.FragmentEventDetailsBinding
import com.kuba.flashscore.local.models.INCIDENTTYPE
import com.kuba.flashscore.local.models.Incident
import com.kuba.flashscore.network.models.events.EventDto
import com.kuba.flashscore.ui.FlashScoreViewModel
import timber.log.Timber


class EventDetailsFragment(private val event: EventDto) :
    Fragment(R.layout.fragment_event_details) {

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventDetailAdapter: EventDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        setInformationAboutRefereeAndStadium()
        sortAllIncidentList()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (event.matchStatus == "Finished") {
            binding.constraintLayoutEventDetailsInformationAboutMatch.visibility = View.VISIBLE
            setInformationAboutFirstHalf()
            setupRecyclerView()
        } else {
            binding.constraintLayoutEventDetailsMessageBeforeMatch.visibility = View.VISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun setInformationAboutFirstHalf() {
        binding.apply {
            textViewInformationAboutFirstHalfResult.text =
                "${event.matchHometeamHalftimeScore} - ${event.matchAwayteamHalftimeScore}"
        }
    }

    private fun setInformationAboutRefereeAndStadium() {
        binding.apply {
            textViewRefereeName.text = event.matchReferee
            textViewStadiumName.text = event.matchStadium
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewEventDetailFirstHour.apply {
            eventDetailAdapter = EventDetailAdapter(requireContext())
            eventDetailAdapter.incidents = sortAllIncidentList()
            adapter = eventDetailAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun sortAllIncidentList(): List<Incident> {
        val incidentList = mutableListOf<Incident>()
        incidentList.apply {
            addAll(getCardsFromEventDtoToIncidentList())
            addAll(getGoalscorersFromEventDtoToIncidentList())
            addAll(getSubstitutionsFromEventDtoToIncidentList())
            sortBy { it.time }
        }
        return incidentList
    }

    private fun getGoalscorersFromEventDtoToIncidentList(): List<Incident> {
        val resultList = mutableListOf<Incident>()
        resultList.addAll(event.goalscorer.map { goal ->
            Incident(
                if (goal.homeScorerId.isNotEmpty()) goal.homeScorer else goal.awayScorer,
                if (goal.homeScorerId.isNotEmpty()) goal.homeAssist else goal.awayAssist,
                goal.time,
                INCIDENTTYPE.GOAL,
                goal.homeScorerId.isNotEmpty()
            )
        })
        return resultList
    }


    private fun getCardsFromEventDtoToIncidentList(): List<Incident> {
        val resultList = mutableListOf<Incident>()
        resultList.addAll(event.cards.map { card ->
            Incident(
                if (card.homeFault.isNotEmpty()) card.homeFault else card.awayFault,
                "",
                card.time,
                if (card.card == "yellow card") INCIDENTTYPE.YELLOW_CARD else INCIDENTTYPE.RED_CARD,
                card.homeFault.isNotEmpty()
            )
        })
        return resultList
    }

    private fun getSubstitutionsFromEventDtoToIncidentList(): List<Incident> {
        val resultList = mutableListOf<Incident>()
        resultList.apply {
            addAll(event.substitutions.home.map { sub ->
                val subPlayer = sub.substitution.split("|")
                Incident(
                    subPlayer[1].trim(),
                    subPlayer[0].trim(),
                    sub.time,
                    INCIDENTTYPE.SUBSTITUTION,
                    true
                )
            })
            addAll(event.substitutions.away.map { sub ->
                val subPlayer = sub.substitution.split("|")
                Incident(
                    subPlayer[1].trim(),
                    subPlayer[0].trim(),
                    sub.time,
                    INCIDENTTYPE.SUBSTITUTION,
                    false
                )
            })
        }
        return resultList
    }
}