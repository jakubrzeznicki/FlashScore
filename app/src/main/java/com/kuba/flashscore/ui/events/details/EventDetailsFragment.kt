package com.kuba.flashscore.ui.events.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.EventDetailAdapter
import com.kuba.flashscore.databinding.FragmentEventDetailsBinding
import com.kuba.flashscore.local.models.entities.event.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
import com.kuba.flashscore.local.models.incident.INCIDENTTYPE
import com.kuba.flashscore.local.models.incident.Incident
import com.kuba.flashscore.local.models.incident.IncidentHeader
import com.kuba.flashscore.local.models.incident.IncidentItem
import com.kuba.flashscore.network.models.events.EventDto
import com.kuba.flashscore.other.Constants.CARD_YELLOW
import com.kuba.flashscore.other.Constants.INCIDENT_HEADER_2_HALF
import com.kuba.flashscore.other.Constants.INCIDENT_HEADER_OVERTIME
import com.kuba.flashscore.other.Constants.MATCH_STATUS_FINISHED
import com.kuba.flashscore.other.Constants.RESULT_NULL_TO_NULL
import com.kuba.flashscore.other.Constants.SUBSTITUTION_DIVIDER
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.ui.events.EventsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class EventDetailsFragment(private val eventDetails: EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions) :
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (eventDetails.eventEntity.matchStatus == MATCH_STATUS_FINISHED) {
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
                "${eventDetails.eventEntity.matchHometeamHalftimeScore} - ${eventDetails.eventEntity.matchAwayteamHalftimeScore}"
        }
    }

    private fun setInformationAboutRefereeAndStadium() {
        binding.apply {
            textViewRefereeName.text = eventDetails.eventEntity.matchReferee
            textViewStadiumName.text = eventDetails.eventEntity.matchStadium
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewEventDetailFirstHour.apply {
            eventDetailAdapter = EventDetailAdapter(requireContext())
            eventDetailAdapter.incidents = addHeadersToIncidentItemList()
            adapter = eventDetailAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    private fun addHeadersToIncidentItemList(): List<Incident> {
        val incidentItemList = sortAllIncidentItemListByTimeListByTime()
        val secondHalfHeader =
            incidentItemList.indexOfFirst { it.time.length == 2 && it.time.toInt() >= 46 }
        val overtimeHeader =
            incidentItemList.indexOfFirst { it.time.length == 2 && it.time.toInt() >= 91 }

        val incidentList = mutableListOf<Incident>()
        for (index in incidentItemList.indices) {
            if (index == secondHalfHeader) {
                incidentList.add(
                    IncidentHeader(
                        INCIDENT_HEADER_2_HALF,
                        incidentItemList[index].score ?: RESULT_NULL_TO_NULL
                    )
                )
            }
            if (index == overtimeHeader) {
                incidentList.add(
                    IncidentHeader(
                        INCIDENT_HEADER_OVERTIME,
                        incidentItemList[index].score ?: RESULT_NULL_TO_NULL
                    )
                )
            }
            incidentList.add(incidentItemList[index])
        }
        return incidentList
    }

    private fun sortAllIncidentItemListByTimeListByTime(): List<IncidentItem> {
        val incidentList = mutableListOf<IncidentItem>()
        incidentList.apply {
            addAll(getCardsFromEventDtoToIncidentList())
            addAll(getGoalscorersFromEventDtoToIncidentList())
            addAll(getSubstitutionsFromEventDtoToIncidentItemList())
            sortBy { it.time }
        }
        return incidentList
    }

    private fun getGoalscorersFromEventDtoToIncidentList(): List<IncidentItem> {
        val goalscorersList = mutableListOf<IncidentItem>()
        goalscorersList.addAll(eventDetails.goalscorers.map { goal ->
            IncidentItem(
                goal.scorerId,
                goal.assistId,
                goal.time,
                INCIDENTTYPE.GOAL,
                goal.whichTeam,
                goal.score
            )
        })
        return goalscorersList
    }


    private fun getCardsFromEventDtoToIncidentList(): List<IncidentItem> {
        val cardsList = mutableListOf<IncidentItem>()
        cardsList.addAll(eventDetails.cards.map { card ->
            IncidentItem(
                card.fault,
                "",
                card.time,
                if (card.card == CARD_YELLOW) INCIDENTTYPE.YELLOW_CARD else INCIDENTTYPE.RED_CARD,
                card.whichTeam
            )
        })
        return cardsList
    }

    private fun getSubstitutionsFromEventDtoToIncidentItemList(): List<IncidentItem> {
        val substitutionsList = mutableListOf<IncidentItem>()
        substitutionsList.apply {
            addAll(eventDetails.substitutions.map { sub ->
                val subPlayer = sub.substitution.split(SUBSTITUTION_DIVIDER)
                IncidentItem(
                    subPlayer[1].trim(),
                    subPlayer[0].trim(),
                    sub.time,
                    INCIDENTTYPE.SUBSTITUTION,
                    true
                )
            })
        }
        return substitutionsList
    }
}